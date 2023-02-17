package com.carry.pr.base.tcp;


import com.carry.pr.base.resolve.ResolverChannel;
import com.carry.pr.protocol.Protocol;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class TcpChannel implements ResolverChannel {

    private SocketChannel javaChannel;
    private TaskContent content;

    private final TcpWorker worker;


    public TcpChannel(TcpWorker worker, SocketChannel javaChannel) {
        this.javaChannel = javaChannel;
        this.worker = worker;
    }

    public TcpWorker getWorker() {
        return worker;
    }

    public Protocol getProtocol(){
        return worker.getBelong().getProtocol();
    }

    public SocketChannel getJavaChannel() {
        return javaChannel;
    }

    public void setJavaChannel(SocketChannel javaChannel) {
        this.javaChannel = javaChannel;
    }

    public TaskContent getContent() {
        return content;
    }

    public void setContent(TaskContent content) {
        this.content = content;
    }

    @Override
    public boolean isOpen() {
        return javaChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        javaChannel.close();
    }
}
