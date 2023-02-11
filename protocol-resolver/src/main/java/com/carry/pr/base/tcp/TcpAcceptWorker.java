package com.carry.pr.base.tcp;

import com.carry.pr.base.task.DefaultTask;
import com.carry.pr.base.executor.WorkGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TcpAcceptWorker extends TcpWorker {

    private static final Logger log = LoggerFactory.getLogger(TcpWorker.class);
    int port;
    ServerSocketChannel serverSocketChannel;
    WorkGroup childGroup;

    public TcpAcceptWorker(int port, WorkGroup workGroup, WorkGroup childGroup) {
        super(workGroup);
        this.port = port;
        this.childGroup = childGroup;
    }

    @Override
    public void start() throws IOException {
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
            TcpWorker worker = (TcpWorker) childGroup.nextWorker();
            worker.execute(new DefaultTask(() -> {
                try {
                    channel.register(worker.selector, SelectionKey.OP_READ, new TcpChannel(worker, channel));
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
