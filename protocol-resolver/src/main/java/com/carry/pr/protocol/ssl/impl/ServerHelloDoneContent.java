package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 此消息完成了握手协商的服务器部分。它不携带任何附加信息
 */
public class ServerHelloDoneContent extends HandShakeContent {


    public ServerHelloDoneContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
