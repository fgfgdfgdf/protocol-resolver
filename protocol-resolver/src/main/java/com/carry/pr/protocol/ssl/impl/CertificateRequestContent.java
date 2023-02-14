package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.protocol.ssl.HandShakeContent;

/**
 * 当服务器需要客户端身份验证时使用。
 * 在web服务器中不常用，但在某些情况下非常重要。
 * 该消息不仅要求客户端提供证书，还告诉哪些证书类型是可接受的。
 * 此外，它还指出哪些证书颁发机构是可信任的。
 *      |
 *      |
 *      |  Handshake Layer
 *      |
 *      |
 * - ---+----+----+----+----+----+----+---- - - --+----+----+----+----+-----------+-- -
 *      | 13 |    |    |    |    |    |           |    |    |    |    |    C.A.   |
 *      |0x0d|    |    |    |    |    |           |    |    |    |    |unique name|
 * - ---+----+----+----+----+----+----+---- - - --+----+----+----+----+-----------+-- -
 *   /  |  \    \---------\    \    \                \----\   \-----\
 *  /       \        \          \ Certificate           \        \
 * record    \     length        \ Type 1 Id        Certificate   \
 * length     \             Certificate         Authorities length \
 *             type: 13     Types length                         Certificate Authority
 *                                                                       length
 */
public class CertificateRequestContent extends HandShakeContent {

    int certificateTypesLength;

    int certificateTypeId;

    int[] certificateAuthLength;


    public CertificateRequestContent() {
    }

    @Override
    protected void decode(ByteBufferPool.ByteBufferCache in) {
        certificateTypesLength=in.readByte();
        certificateTypeId=in.readByte();



    }
}
