package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;
import com.carry.pr.protocol.ssl.SSLRecord;

import java.nio.charset.StandardCharsets;

/**
 * 此消息通常开始TLS握手协商。
 * 发送时附带一个客户端支持的密码套件列表，供服务器选择最合适的(最好是最强的)、
 * 一个压缩方法列表和一个扩展列表。
 * 通过包含SessionId字段，它还为客户端提供了重新启动前一个会话的可能性。
 * |
 * |  Handshake Layer
 * |
 * |
 * - ---+----+----+----+----+----+----+------+----+----------+--------+-----------+----------+
 * |  1 |    |    |    |    |    |32-bit|    |max 32-bit| Cipher |Compression|Extensions|
 * |0x01|    |    |    |  3 |  1 |random|    |session Id| Suites |  methods  |          |
 * - ---+----+----+----+----+----+----+------+----+----------+--------+-----------+----------+
 * /  |  \    \---------\    \----\            \       \
 * /       \        \            \               \   SessionId
 * record    \     length        SSL/TLS           \
 * length     \                  version         SessionId
 * type: 1       (TLS 1.0 here)       length
 */
public class ClientHelloContent extends HandShakeContent {

    SSLRecord.Version version;

    byte[] random = new byte[32];
    String randomStr;

    int sessionLength;

    int sessionId;

    CipherSuites cipherSuites;

    CompressionMethod compressionMethods;

    Extensions exception;

    public ClientHelloContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {
        recordOrigin(in);

        version = SSLRecord.Version.valueOf(in.readByte(), in.readByte());
        for (int i = 0; i < random.length; i++) {
            random[i] = in.readByte();
        }
        randomStr = new String(random, StandardCharsets.UTF_8);

        sessionLength = in.readByte();
        for (int i = 0; i < sessionLength; i++) {
            sessionId |= in.readByte() << i;
        }
        cipherSuites = CipherSuites.decode(in);
        compressionMethods = CompressionMethod.decode(in);
        exception = Extensions.decode(in);
    }
}
