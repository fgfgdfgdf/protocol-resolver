package com.carry.pr.base.tcp;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.task.DefaultTask;
import com.carry.pr.base.task.Task;
import com.carry.pr.base.executor.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public final class TcpReadTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(TcpReadTask.class);

    private final TaskContent content;
    private boolean readOver;

    private ReadHandle handle;
    private final SelectionKey selectionKey;

    public TcpReadTask(TcpChannel tcpChannel, SelectionKey selectionKey) {
        TaskContent taskContent = tcpChannel.getContent();
        if (taskContent == null) {
            taskContent = new TaskContent(tcpChannel, tcpChannel.getWorker());
            tcpChannel.setContent(taskContent);
            taskContent.protocol = tcpChannel.getProtocol();
            taskContent.tcpChannel = tcpChannel;
        }
        this.content = taskContent;
        this.selectionKey = selectionKey;
    }

    @Override
    public Task next(Worker worker) {
        if (readOver) {
            ensureCacheRecycle();
            return new TcpWriteTask(content);
        }
        return null;
    }

    @Override
    public Task exception() {
        return new DefaultTask(this::ensureCacheRecycle);
    }

    @Override
    public void run() {
        SocketChannel socketChannel = content.tcpChannel.getJavaChannel();
        boolean close = false;
        int totalRead = 0, capacity, read;
        try {
            do {
                ensureCacheUse();
                capacity = content.in.getByteBuffer().remaining();
                read = socketChannel.read(content.in.getByteBuffer());
                if (read < 0) {
                    close = true;
                    break;
                }
                System.out.print(" rindex:" + content.in.getrIndex());
                System.out.print(" windex:" + content.in.getwIndex());
                System.out.print(" postion:" + content.in.getByteBuffer().position());
                System.out.print(" limit:" + content.in.getByteBuffer().limit());
                System.out.print(" cap:" + content.in.getByteBuffer().capacity());
                System.out.println();
                totalRead += read;
            } while (read != 0 && read == capacity);

            if (totalRead != 0) {
                ReadHandle readHandle = content.protocol.getReadHandle();
                if (readHandle != null && readHandle.rhandle(content)) {
                    content.in.recycle();
                    content.in = null;
                    readOver();
                }
            }
            if (close) {
                ensureCacheRecycle();
                content.tcpChannel.close();
            }
        } catch (IOException e) {
            try {
                ensureCacheRecycle();
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void readOver() {
        readOver = true;
    }

    public void ensureCacheUse() {
        if (content.in == null) {
            content.in = ByteBufferPool.smallest(content);
        } else if (!content.in.getByteBuffer().hasRemaining()) {
            content.in = ByteBufferPool.grew(content, content.in);
        }
    }

    public void ensureCacheRecycle() {
        if (content.in != null) {
            content.in.recycle();
            content.in = null;
        }
    }

    public void printIn(ByteBuffer in) {
        if (in == null) return;
        int position = in.position();
        for (int i = 0; i < position; i++) {
            System.out.print(((char) in.get(i)));
        }
    }

    public interface ReadHandle {
        /**
         * @return readOver
         */
        boolean rhandle(TaskContent content);
    }

    public static class TcpDefaultReadHandle implements ReadHandle {

        public static TcpDefaultReadHandle instance = new TcpDefaultReadHandle();

        @Override
        public boolean rhandle(TaskContent content) {
            ByteBufferPool.ByteBufferCache in = content.in;
            if (in != null) {
                StringBuilder sb = new StringBuilder();
                while (in.ensureRead(1)) {
                    sb.append((char) in.readByte());
                }
                content.getObjList().add(sb);
            }
            return false;
        }
    }

}
