package com.carry.pr.base.tcp;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.task.DefaultTask;
import com.carry.pr.base.task.Task;
import com.carry.pr.base.executor.Worker;
import com.carry.pr.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        Protocol protocol = content.protocol;
        ReadHandle readHandle = protocol.readHandle;
        boolean close = false;
        int capacity, read;
        try {
            do {
                ensureCacheUse();
                capacity = content.in.getByteBuffer().remaining();
                read = socketChannel.read(content.in.getByteBuffer());
                if (read < 0) {
                    close = true;
                    break;
                }
                // read Handle
                if (readHandle != null && readHandle.rhandle(content)) {
                    content.in.recycle();
                    content.in = null;
                    readOver();
                    break;
                }
            } while (read != 0 && read == capacity);

            if (close) {
                System.out.println(Thread.currentThread().getName() + " close channel:" + socketChannel);
                ensureCacheRecycle();
                content.tcpChannel.close();
            }
        } catch (IOException e) {
            try {
                socketChannel.close();
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

    public interface ReadHandle {
        /**
         * @return readOver
         */
        boolean rhandle(TaskContent content);
    }

    public static class TcpDefaultReadHandle implements ReadHandle {

        public final static TcpDefaultReadHandle instance = new TcpDefaultReadHandle();

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
