package com.carry.pr.tcp;

import com.carry.pr.base.Task;
import com.carry.pr.base.WorkGroup;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpAcceptTask extends TcpTask {

    int port;
    WorkGroup childGroup;
    Selector selector;
    AtomicBoolean run = new AtomicBoolean();

    public TcpAcceptTask(int port, WorkGroup childGroup) {
        this.port = port;
        this.childGroup = childGroup;
    }

    @Override
    public void run() {
        try {
            if (!run.get()) {
                init();
            }

            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                if(next.isAcceptable()){
                    ServerSocketChannel channel =(ServerSocketChannel) next.channel();
                    SocketChannel accept = channel.accept();
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void init() throws Exception {
        Selector open = Selector.open();
        this.selector = open;
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(port));
        run.compareAndSet(false, true);
    }


    @Override
    public Task next() {
        return this;
    }
}
