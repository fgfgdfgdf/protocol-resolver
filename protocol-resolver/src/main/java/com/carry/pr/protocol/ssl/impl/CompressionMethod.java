package com.carry.pr.protocol.ssl.impl;


import com.carry.pr.base.bytes.ByteBufferPool;

/**
 * Compression methods (no practical implementation uses compression)
 * <p>
 * +----+----+----+
 * |    |    |    |
 * |  0 |  1 |  0 |
 * +----+----+----+
 * \-------\    \
 * \        \
 * length: 1    cmp Id: 0
 */
public class CompressionMethod {

    int length;

    int cmpId;

    public static CompressionMethod decode(ByteBufferPool.ByteBufferCache in) {
        CompressionMethod compressionMethod = new CompressionMethod();
        compressionMethod.length = in.readByte();
        compressionMethod.cmpId = in.readByte();
        return compressionMethod;
    }
}
