package com.carry.pr.base.tcp;

import com.carry.pr.base.executor.Task;
import com.carry.pr.base.executor.Worker;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class TcpWriteTask implements Task {

    TaskContent content;

    public TcpWriteTask(TaskContent content) {
        this.content = content;
    }


    @Override
    public Task next(Worker worker) {
        tryWrite(worker);
        return null;
    }

    @Override
    public Task exception() {
        return null;
    }

    @Override
    public void run() {
        System.out.println("写任务run");
        content.data = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));
    }


    public void tryWrite(Worker worker) {
        try {
            ByteBuffer bytes = content.data;
            SocketChannel socketChannel = content.socketChannel;
            int remaining = bytes.remaining();
            int tryCount = 16;
            do {
//                int write = socketChannel.write(bytes);
//                remaining -= write;
                tryCount--;
            } while (remaining > 0 && tryCount > 0);
            if (remaining > 0 && worker instanceof TcpWorker) {
                TcpWorker tcpWorker = (TcpWorker) worker;
                try {
                    content.socketChannel.register(tcpWorker.selector, SelectionKey.OP_WRITE, content);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else {
                content.socketChannel.register(((TcpWorker) worker).selector, SelectionKey.OP_READ, content);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
