package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

import java.security.cert.Certificate;

/**
 * 此消息正文包含一个公钥证书链。证书链允许TLS支持证书层次结构和pki(公钥基础设施)。
 * |
 * |  Handshake Layer
 * |
 * |
 * - ---+----+----+----+----+----+----+----+----+----+----+-----------+---- - -
 * | 11 |    |    |    |    |    |    |    |    |    |           |
 * |0x0b|    |    |    |    |    |    |    |    |    |certificate| ...more certificate
 * - ---+----+----+----+----+----+----+----+----+----+----+-----------+---- - -
 * /  |  \    \---------\    \---------\    \---------\
 * /       \        \              \              \
 * record    \     length      Certificate    Certificate
 * length     \                   chain         length
 * type: 11           length
 */
public class CertificateContent extends HandShakeContent {

    int certificateChainLength;

    int certificateLength;

    Certificate[] certificates;

    public CertificateContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
