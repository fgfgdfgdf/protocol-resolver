package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 客户端使用此消息向服务器证明它拥有与其公钥证书相对应的私钥。该消息包含由客户端数字签名的散列信息。
 * 如果服务器向客户端发出CertificateRequest，则需要发送一个需要验证的证书。
 * 同样，信息的确切大小和结构取决于商定的算法。在所有情况下，作为哈希函数输入的信息都是相同的。
 * |
 * |
 * |  Handshake Layer
 * |
 * |
 * - ---+----+----+----+----+----------+
 * | 15 |    |    |    |  signed  |
 * |0x0f|    |    |    |   hash   |
 * - ---+----+----+----+----+----------+
 * /  |  \    \---------\
 * /       \        \
 * record    \     length
 * length     \
 * type: 15
 */
public class CertificateVerifyContent extends HandShakeContent {

    byte[] signedHash;

    public CertificateVerifyContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {
        signedHash = new byte[totalLength];
        for (int i = 0; i < signedHash.length; i++) {
            signedHash[i] = in.readByte();
        }

    }
}
