package com.carry.pr.tcp;


import java.nio.channels.SocketChannel;

public class TcpRegisterTask extends TcpTask {

    SocketChannel channel;

    public TcpRegisterTask(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
    }
}
