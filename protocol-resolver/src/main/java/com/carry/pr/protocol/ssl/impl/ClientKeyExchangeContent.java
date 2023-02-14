package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 它为服务器提供生成对称加密密钥所需的数据。
 * 消息格式与ServerKeyExchange非常相似，因为它主要依赖于服务器选择的密钥交换算法。
 *      |
 *      |
 *      |  Handshake Layer
 *      |
 *      |
 * - ---+----+----+----+----+----------------+
 *      | 16 |    |    |    |   algorithm    |
 *      |0x10|    |    |    |   parameters   |
 * - ---+----+----+----+----+----------------+
 *   /  |  \    \---------\
 *  /       \        \
 * record    \     length
 * length     \
 *             type: 16
 */
public class ClientKeyExchangeContent extends HandShakeContent {

    byte[] algorithmParame;


    public ClientKeyExchangeContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
