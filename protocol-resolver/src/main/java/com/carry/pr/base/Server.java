package com.carry.pr.base;



import com.carry.pr.base.executor.WorkGroup;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public abstract class Server {

    protected int port;

    public Server(int port) {
        this.port = port;
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;


    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private WorkGroup bossGroup;
    private WorkGroup workerGroup;

    public void tcpStart() throws Exception {
    }

}
