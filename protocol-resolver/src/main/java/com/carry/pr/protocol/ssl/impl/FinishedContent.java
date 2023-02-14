package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 此消息表示TLS协商已完成，密码套件已被激活。
 * 它应该已经加密发送，因为协商已经成功完成，所以必须在此之前发送一个ChangeCipherSpec协议消息来激活加密。
 * Finished消息包含以前所有握手消息的哈希值，后面是一个特殊的数字，用于标识服务器/客户端角色、主秘密和填充。
 * 结果哈希与CertificateVerify哈希不同，因为有更多的握手消息。
 *      |
 *      |
 *      |  Handshake Layer
 *      |
 *      |
 * - ---+----+----+----+----+----------+
 *      | 20 |    |    |    |  signed  |
 *      |0x14|    |    |    |   hash   |
 * - ---+----+----+----+----+----------+
 *   /  |  \    \---------\
 *  /       \        \
 * record    \     length
 * length     \
 *             type: 20
 */
public class FinishedContent extends HandShakeContent {

    public FinishedContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
