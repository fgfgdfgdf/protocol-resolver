package com.carry.pr.base.bytes;

import com.carry.pr.base.tcp.TaskContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteBufferPool {

    private static final Logger log = LoggerFactory.getLogger(ByteBufferPool.class);

    public static final int INIT_SCALE = 1024;
    public static final int INIT_SIZE = Runtime.getRuntime().availableProcessors();

    private static final AtomicInteger incrId = new AtomicInteger();
    private static final ConcurrentHashMap<Integer, Integer> useMap = new ConcurrentHashMap<>();

    public static ByteBufferCache smallest(TaskContent content) {
        ByteBufferCache cache = null;
        try {
            cache = LvCachePool.LV1.get();
            useMap.put(cache.id, content.getId());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        print();
        return cache;
    }

    public static ByteBufferCache optimalSize(TaskContent content, int scale) {
        ByteBufferCache cache = null;
        try {
            LvCachePool next = getNext(scale);
            if (next == null) {
                cache = new ByteBufferCache(scale);
            } else {
                cache = next.get();
            }
            useMap.put(cache.id, content.getId());
            print();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return cache;
    }

    public static ByteBufferCache grew(TaskContent content, ByteBufferCache cache) {
        try {
            LvCachePool next = getNext(cache.pool.scale);
            ByteBufferCache nextCache;
            if (next == null) {
                int scale = cache.byteBuffer.capacity();
                // 直接使用堆内bytebuffer
                nextCache = new ByteBufferCache(scale << 1);
            } else {
                nextCache = next.get();
                useMap.put(nextCache.id, content.getId());
            }
            // copy
            ByteBuffer byteBuffer = cache.byteBuffer;
            byteBuffer.flip();
            ByteBuffer newByteBuffer = nextCache.byteBuffer;
            newByteBuffer.put(byteBuffer);
            newByteBuffer.position(byteBuffer.position());
            cache.recycle();
            return nextCache;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    private static LvCachePool getNext(int scale) {
        LvCachePool[] values = LvCachePool.values();
        LvCachePool next = null;
        for (LvCachePool value : values) {
            if (value.scale > scale) {
                return value;
            }
        }
        return null;
    }


    public interface CachePool<T> {
        void recycle(T t) throws InterruptedException;

        T get() throws Exception;
    }

    enum LvCachePool implements CachePool<ByteBufferCache> {
        LV1, LV2, LV3;
        private final BlockingQueue<ByteBufferCache> freeQueue = new ArrayBlockingQueue<>(INIT_SIZE);

        final int scale;

        LvCachePool() {
            scale = INIT_SCALE << ordinal();
            for (int i = 0; i < INIT_SIZE; i++) {
                boolean offer = freeQueue.offer(new ByteBufferCache(incrId.incrementAndGet(), this).init());
            }
        }

        @Override
        public void recycle(ByteBufferCache cache) throws InterruptedException {
            ByteBufferPool.useMap.remove(cache.id);
            ByteBuffer byteBuffer = cache.byteBuffer;
            byteBuffer.clear();
            cache.useId = 0;
            cache.pool = this;
            freeQueue.put(cache);
        }

        @Override
        public ByteBufferCache get() throws Exception {
            ByteBufferCache cache;
            if ((cache = freeQueue.peek()) != null) {
                cache.init();
                return freeQueue.poll();
            }
            return freeQueue.take();
        }
    }

    public static class ByteBufferCache implements BytesRW {
        int id;
        int useId;
        ByteBuffer byteBuffer;
        LvCachePool pool;

        @Override
        public boolean isBigEndian() {
            return true;
        }

        @Override
        public byte readByte() {
            return byteBuffer.get();
        }

        @Override
        public void writeByte(byte b) {
            byteBuffer.put(b);
        }

        public boolean ensureRW(int byteSize) {
            int position = byteBuffer.position();
            int limit = byteBuffer.limit();
            return position + byteSize <= limit;
        }

        private ByteBufferCache(int id, LvCachePool pool) {
            this.id = id;
            this.pool = pool;
        }

        private ByteBufferCache init() {
            if (byteBuffer == null)
                this.byteBuffer = ByteBuffer.allocateDirect(pool.scale);
            return this;
        }

        private ByteBufferCache(int size) {
            this.byteBuffer = ByteBuffer.allocate(size);
        }

        public void recycle() {
            try {
                if (byteBuffer.isDirect()) {
                    pool.recycle(this);
                } else {
                    byteBuffer = null;
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

        }

        public ByteBuffer getByteBuffer() {
            return byteBuffer;
        }
    }

    public static void print() {
        for (LvCachePool item : LvCachePool.values()) {
//            log.info("LvCachePool {},thread {},size:{},free:{}", item.name(), Thread.currentThread().getName(), INIT_SIZE, item.freeQueue.size());
        }
    }

}
