package com.carry.pr.base;


import com.carry.pr.tcp.TcpAcceptTask;
import com.carry.pr.tcp.TcpAcceptWorker;
import com.carry.pr.tcp.TcpWorker;

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
        bossGroup=new WorkGroup(1);
        bossGroup.init(TcpAcceptWorker.class);
        bossGroup.execute(new TcpAcceptTask(port, workerGroup));

        workerGroup=new WorkGroup(1);
        workerGroup.init(TcpWorker.class);
    }

}
