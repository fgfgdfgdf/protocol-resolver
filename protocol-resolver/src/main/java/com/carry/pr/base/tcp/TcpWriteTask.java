package com.carry.pr.base.tcp;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.task.Task;
import com.carry.pr.base.executor.Worker;
import com.carry.pr.protocol.Protocol;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class TcpWriteTask implements Task {

    TaskContent content;

    boolean write;

    public TcpWriteTask(TaskContent content) {
        this.content = content;
    }

    @Override
    public Task next(Worker worker) {
        return null;
    }

    @Override
    public Task exception() {
        return null;
    }

    @Override
    public void run() {
        Protocol protocol = content.protocol;
        WriteHandle writeHandle = protocol.getWriteHandle();
        if (writeHandle != null) {
            if (writeHandle.whandle(content))
                tryWrite(content.worker);
        }
    }


    public void tryWrite(Worker worker) {
        boolean writeOver = true;
        try {
            if (content.out == null) return;
            ByteBuffer bytes = content.out.getByteBuffer();
            bytes.flip();
            SocketChannel socketChannel = content.tcpChannel.getJavaChannel();
            int remaining = bytes.remaining();
            int tryCount = 16;
            do {
                int write = socketChannel.write(bytes);
                remaining -= write;
                tryCount--;
            } while (remaining > 0 && tryCount > 0);

            if (remaining > 0) {
                try {
                    writeOver = false;
                    socketChannel.register(((TcpWorker) worker).selector, SelectionKey.OP_WRITE, content.tcpChannel);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (content.in != null) {
                content.in.recycle();
                content.in = null;
            }
            if (content.out != null && writeOver) {
                content.out.recycle();
                content.out = null;
            }
            content.getObjList().clear();
        }
    }

    public interface WriteHandle {
        /**
         * @return need sockerChannel.write()
         */
        boolean whandle(TaskContent content);
    }

    public static class TcpDefaultWriteHandle implements WriteHandle {

        public static TcpDefaultWriteHandle instance = new TcpDefaultWriteHandle();

        @Override
        public boolean whandle(TaskContent content) {
            StringBuilder obj = content.getObj(StringBuilder.class);
            if (obj != null) {
                byte[] bytes = obj.toString().getBytes(StandardCharsets.UTF_8);
                content.out = ByteBufferPool.optimalSize(content, bytes.length);
                content.out.getByteBuffer().put(bytes);
                content.getObjList().remove(obj);
            }
            return true;
        }
    }
}
