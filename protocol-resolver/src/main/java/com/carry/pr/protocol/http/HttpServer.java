package com.carry.pr.protocol.http;

import com.carry.pr.base.Server;
import com.carry.pr.base.WorkGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.*;

public class HttpServer extends Server {

    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private WorkGroup bossGroup;
    private WorkGroup workerGroup;

    public HttpServer(int port) {
        super(port);
    }

    @Override
    public void start() throws Exception {
        tcpStart();
    }

    @Override
    public void stop() throws Exception {
        selector.close();
        serverSocketChannel.close();
        log.info("HttpServer closed!");
    }
}
