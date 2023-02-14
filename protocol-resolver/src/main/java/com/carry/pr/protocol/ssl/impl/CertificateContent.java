package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;

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
        certificateChainLength = in.readByte() | in.readByte() << 8 | in.readByte() << 16;
        certificateLength = in.readByte() | in.readByte() << 8 | in.readByte() << 16;
        certificates = new Certificate[certificateChainLength];

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certCollection = cf.generateCertificates(in);
            certificates = certCollection.toArray(certificates);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
}
