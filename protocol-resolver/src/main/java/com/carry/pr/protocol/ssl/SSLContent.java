package com.carry.pr.protocol.ssl;

import com.carry.pr.base.bytes.ByteBufferPool;

import java.util.function.BiFunction;

public class SSLContent {

    SSLRecord record;

    SSLHandShake handShake;

    SSLResolver sslResolve = SSLResolver.RECORE_TYPE;


    public boolean read(ByteBufferPool.ByteBufferCache in) {
        boolean over;
        do {
            over = sslResolve.resolverFunc.apply(this, in);
        } while (over);
        return over;
    }


    public SSLRecord getRecord() {
        if (record == null)
            record = new SSLRecord();
        return record;
    }

    public SSLHandShake getHandShake() {
        if (handShake == null)
            handShake = new SSLHandShake();
        return handShake;
    }

    enum SSLResolver {
        // -------------------------record-------------------
        RECORE_TYPE((content, in) -> {
            if (!in.ensureRead(1)) return false;
            SSLRecord sslRecord = content.getRecord();
            sslRecord.recordType = SSLRecord.RecordType.valueOf(in.readByte());
            return true;
        }),
        VERSION((content, in) -> {
            if (!in.ensureRead(2)) return false;
            SSLRecord sslRecord = content.getRecord();
            sslRecord.version = SSLRecord.Version.valueOf(in.readShort());
            return true;
        }),
        LENGTH((content, in) -> {
            if (!in.ensureRead(2)) return false;
            SSLRecord sslRecord = content.getRecord();
            sslRecord.length = in.readShort();
            return true;
        }),
        // -------------------------handShake-------------------
        HANDSHAKE_TYPE((content, in) -> {
            if (!in.ensureRead(1)) return false;
            SSLHandShake handShake = content.getHandShake();
            handShake.handShakeType = SSLHandShake.HandShakeType.valueOf(in.readByte());
            return true;
        }),
        HANDSHAKE_LENGTH((content, in) -> {
            if (!in.ensureRead(3)) return false;
            SSLHandShake handShake = content.getHandShake();
            handShake.length = in.readByte() + in.readByte() << 8 + in.readByte() << 16;
            return true;
        }),
        HANDSHAKE_CONTENT((content, in) -> {
            if (!in.ensureRead(content.getHandShake().length)) return false;
            SSLHandShake handShake = content.getHandShake();
            handShake.length = in.readByte() + in.readByte() << 8 + in.readByte() << 16;
            return true;
        }),

        ;
        final BiFunction<SSLContent, ByteBufferPool.ByteBufferCache, Boolean> resolverFunc;

        SSLResolver(BiFunction<SSLContent, ByteBufferPool.ByteBufferCache, Boolean> resolverFunc) {
            this.resolverFunc = resolverFunc;
        }

        public SSLResolver next() {
            SSLResolver[] values = SSLResolver.values();
            for (int i = 0; i < values.length; i++) {
                if (values[i] == this && i + 1 < values.length) {
                    return values[i + 1];
                }
            }
            return null;
        }

    }


}
