package com.carry.pr.tcp;

import java.nio.channels.ServerSocketChannel;

public class TcpAcceptWorker extends TcpWorker{

    ServerSocketChannel serverSocketChannel;

    public TcpAcceptWorker() {
    }

    @Override
    protected void init() throws Exception {
        super.init();
    }


}
