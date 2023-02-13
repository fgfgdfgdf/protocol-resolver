package com.carry.pr.protocol.ssl.impl;

/**
 * CipherSuites
 * +----+----+----+----+----+----+
 * |    |    |    |    |    |    |
 * |    |    |    |    |    |    |
 * +----+----+----+----+----+----+
 * \-----\   \-----\    \----\
 *    \         \          \
 * length    cipher Id  cipherId
 */
public class CipherSuites {
    int length;
    int cipherId1;
    int cipherId2;
}
