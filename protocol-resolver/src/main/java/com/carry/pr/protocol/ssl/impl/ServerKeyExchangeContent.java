package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 此消息携带客户端需要从服务器获取的密钥交换算法参数，以便在此之后使对称加密工作。
 * 这是可选的，因为并非所有密钥交换都要求服务器显式地发送此消息。
 * 实际上，在大多数情况下，证书消息足以让客户机安全地与服务器通信预主密钥。
 * 这些参数的格式完全取决于所选的密码套件，该密码套件之前已由服务器通过ServerHello消息设置
 *
 *      |
 *      |
 *      |
 *      |  Handshake Layer
 *      |
 *      |
 * - ---+----+----+----+----+----------------+
 *      | 12 |    |    |    |   algorithm    |
 *      |0x0c|    |    |    |   parameters   |
 * - ---+----+----+----+----+----------------+
 *   /  |  \    \---------\
 *  /       \        \
 * record    \     length
 * length     \
 *             type: 12
 */
public class ServerKeyExchangeContent extends HandShakeContent {

    byte[] algorithmParam;

    public ServerKeyExchangeContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
