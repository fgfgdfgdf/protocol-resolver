package com.carry.pr.base.tcp;

import com.carry.pr.base.executor.Task;

import java.nio.ByteBuffer;


public class TcpTask implements Task {

    ByteBuffer data;

    public TcpTask(ByteBuffer data) {
        this.data = data;
    }

    @Override
    public Task next() {
        return null;
    }

    @Override
    public Task exception() {
        return null;
    }

    @Override
    public void run() {
        while (data.hasRemaining()){
            System.out.print((char)data.get());
        }
    }
}