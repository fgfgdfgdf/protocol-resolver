package com.carry.pr.base.tcp;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TaskContent {

    ByteBuffer data;

    SocketChannel socketChannel;

    public TaskContent(SocketChannel socketChannel, ByteBuffer data) {
        this.socketChannel = socketChannel;
        this.data = data;
    }
}
