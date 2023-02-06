package com.carry.pr.base.tcp;

import com.carry.pr.base.executor.Task;
import com.carry.pr.base.executor.Worker;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class TcpReadTask implements Task {

    TaskContent content;

    public TcpReadTask(ByteBuffer data, SocketChannel socketChannel) {
        this.content = new TaskContent(socketChannel, data);
    }

    @Override
    public Task next(Worker worker) {
        return new TcpWriteTask(content);
    }

    @Override
    public Task exception() {
        return null;
    }

    @Override
    public void run() {
        ByteBuffer data = content.data;
        while (data.hasRemaining()) {
            System.out.print((char) data.get());
        }
        System.out.println();
    }

}
