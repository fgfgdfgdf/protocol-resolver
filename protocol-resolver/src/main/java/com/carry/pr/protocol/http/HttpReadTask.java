package com.carry.pr.protocol.http;

import com.carry.pr.base.tcp.TcpReadTask;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class HttpReadTask extends TcpReadTask {

    public HttpReadTask(ByteBuffer data, SocketChannel socketChannel) {
        super(data, socketChannel);
    }
}
