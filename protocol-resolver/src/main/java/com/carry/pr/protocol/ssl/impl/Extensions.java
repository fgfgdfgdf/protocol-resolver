package com.carry.pr.protocol.ssl.impl;

import com.carry.pr.base.bytes.ByteBufferPool;

/**
 * +----+----+----+----+----+----+----- - -
 * |    |    |    |    |    |    |
 * |    |    |    |    |    |    |...extension data
 * +----+----+----+----+----+----+----- - -
 * \-----\   \-----\    \----\
 * \         \          \
 * length    Extension  Extension data
 * Id          length
 */
public class Extensions {
    int length;

    Extension[] extension;

    public static Extensions decode(ByteBufferPool.ByteBufferCache in) {
        Extensions extensions = new Extensions();
        extensions.length = in.readShort();
        int read = 0;
        while (read >= extensions.length) {
            Extension ets = new Extension();
            ets.type = Type.valueOf(in.readShort());
            read += 2;
            ets.length = in.readShort();
            read += 2;
            ets.data = new byte[ets.length];
            read += ets.length;
            for (int i = 0; i < ets.length; i++) {
                ets.data[i] = in.readByte();
            }
        }
        return extensions;
    }

    public static class Extension {
        Type type;
        int length;
        byte[] data;
    }


    enum Type {
        UNKOWN(0);;
        int value;

        Type(int value) {
            this.value = value;
        }

        static Type valueOf(int value) {
            for (Type t : values()) {
                if (t.value == value) {
                    return t;
                }
            }
            return UNKOWN;
        }

    }

}
