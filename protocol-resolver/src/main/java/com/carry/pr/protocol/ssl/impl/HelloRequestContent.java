package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 允许服务器重新启动握手协商。不常用。
 * 如果连接已经启动了足够长的时间，以至于其安全性被削弱(以小时为单位)，服务器可以使用此消息强制客户端重新协商新的会话密钥。
 *     |
 *     |  Handshake Layer
 *     |
 *     |
 * ----+----+----+----+----+
 *     |    |    |    |    |
 *   4 |  0 |  0 |  0 |  0 |
 * ----+----+----+---- +----+
 *  /  |   \  \---------\
 * /        \       \
 * record    \    length: 0
 * length     \
 *           type: 0
 */
public class HelloRequestContent extends HandShakeContent {

    // nothing...

    public HelloRequestContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
