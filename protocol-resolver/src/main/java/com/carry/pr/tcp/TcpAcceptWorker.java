package com.carry.pr.tcp;

import com.carry.pr.base.Task;
import com.carry.pr.base.WorkGroup;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TcpAcceptWorker extends TcpWorker {

    int port;
    ServerSocketChannel serverSocketChannel;
    WorkGroup childGroup;

    public TcpAcceptWorker(int port, WorkGroup childGroup) {
        this.port = port;
        this.childGroup = childGroup;
    }

    @Override
    protected void start() throws Exception {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        super.start();
    }

    @Override
    public void accept(SelectionKey selectionKey) {
        try {
            ServerSocketChannel serverchannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel channel = serverchannel.accept();
            channel.configureBlocking(false);
            TcpWorker worker =(TcpWorker) childGroup.nextWorker();
            worker.execute(new Task() {
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
                    try {
                        channel.register(worker.selector,SelectionKey.OP_READ);
                    } catch (ClosedChannelException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
