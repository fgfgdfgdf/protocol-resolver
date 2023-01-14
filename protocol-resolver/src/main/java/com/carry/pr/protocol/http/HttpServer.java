package com.carry.pr.protocol.http;

import com.carry.pr.base.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class HttpServer extends Server implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    Thread thread;

    public HttpServer(int port) {
        super(port);
    }

    @Override
    public void start() throws Exception {
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        this.selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(port));

        thread = new Thread(this, "HttpServer Thread-" + port);
        thread.start();
        log.info("HttpServer start!");
    }

    @Override
    public void run() {
        log.info("HttpServer: accept thread start work !");

        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    if (next.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                        SocketChannel acceptd = channel.accept();

                    }
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        selector.close();
        serverSocketChannel.close();
        log.info("HttpServer closed!");
    }
}
