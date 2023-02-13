package com.carry.pr.protocol.ssl.impl;

/**
 * +----+----+----+----+----+----+----- - -
 * |    |    |    |    |    |    |
 * |    |    |    |    |    |    |...extension data
 * +----+----+----+----+----+----+----- - -
 * \-----\   \-----\    \----\
 *    \         \          \
 * length    Extension  Extension data
 * Id          length
 */
public class Extensions {
    int length;
    int extensionId;
    int extensionDataLength;

}
