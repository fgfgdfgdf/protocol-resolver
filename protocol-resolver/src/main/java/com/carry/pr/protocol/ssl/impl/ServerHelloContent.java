package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;
import com.carry.pr.protocol.ssl.SSLRecord;

/**
 * ServerHello消息与ClientHello消息非常相似，不同之处是它只包含一个CipherSuite和一个Compression方法。
 * 如果它包含一个SessionId(即SessionId长度为> 0)，它会向客户端发出信号，让客户端在未来尝试重用它。
 * <p/>
 *      |
 *      |
 *      |  Handshake Layer
 *      |
 *      |
 * - ---+----+----+----+----+----+----+----------+----+----------+----+----+----+----------+
 *      |  2 |    |    |    |    |    |  32byte  |    |max 32byte|    |    |    |Extensions|
 *      |0x02|    |    |    |  3 |  1 |  random  |    |session Id|    |    |    |          |
 * - ---+----+----+----+----+----+----+----------+----+----------+--------------+----------+
 *   /  |  \    \---------\    \----\               \       \       \----\    \
 *  /       \        \            \                  \   SessionId      \  Compression
 * record    \     length        SSL/TLS              \ (if length > 0)  \   method
 * length     \                  version           SessionId              \
 *             type: 2       (TLS 1.0 here)         length            CipherSuite
 */
public class ServerHelloContent  extends HandShakeContent {

    SSLRecord.Version version;

    byte[] random = new byte[32];

    int sessionLength;

    int sessionId;

    CipherSuites cipherSuites;

    CompressionMethod compressionMethods;

    Exception exception;

    public ServerHelloContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {

    }
}
