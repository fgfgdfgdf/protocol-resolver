package com.carry.pr.protocol.ssl.impl;


/**
 * Compression methods (no practical implementation uses compression)
 *
 *  +----+----+----+
 *  |    |    |    |
 *  |  0 |  1 |  0 |
 *  +----+----+----+
 *  \-------\    \
 *      \        \
 *  length: 1    cmp Id: 0
 *
 */
public class CompressionMethod {

    int length;

    int cmpId;
}
