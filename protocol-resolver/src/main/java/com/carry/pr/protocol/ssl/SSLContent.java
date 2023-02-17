package com.carry.pr.protocol.ssl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.bytes.OriginalData;
import com.carry.pr.base.resolve.MsgReqObj;
import com.carry.pr.base.resolve.MsgRespObj;
import com.carry.pr.base.resolve.ResolveChain;

import java.util.function.BiFunction;

public class SSLContent implements MsgReqObj, MsgRespObj, OriginalData {

    SSLRecord record;

    SSLHandShake handShake;

    SSLResolver sslResolve = SSLResolver.RECORE_TYPE;


    public boolean read(ByteBufferPool.ByteBufferCache in) {
        boolean over;
        do {
            over = sslResolve.resolverFunc.apply(this, in);
        } while (over && (sslResolve = sslResolve.next()) != null);
        return over;
    }

    @Override
    public byte[] original() {
        return new byte[0];
    }

    @Override
    public boolean decode(ByteBufferPool.ByteBufferCache in) {
        return false;
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

    enum SSLResolver implements ResolveChain<SSLContent> {
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
            sslRecord.version = SSLRecord.Version.valueOf(in.readByte(), in.readByte());
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
            handShake.length = (in.readByte() << 16 & 0xffffff) | (in.readByte() << 8 & 0xffff) | (in.readByte() & 0xff);
            return true;
        }),
        HANDSHAKE_CONTENT((content, in) -> {
            SSLHandShake handShake = content.getHandShake();
            if (!in.ensureRead(handShake.length)) return false;
            handShake.content = handShake.handShakeType.createContent();
            handShake.content.setTotalLength(handShake.length);
            handShake.content.decode(in);
            return true;
        }),

        ;
        final BiFunction<SSLContent, ByteBufferPool.ByteBufferCache, Boolean> resolverFunc;

        SSLResolver(BiFunction<SSLContent, ByteBufferPool.ByteBufferCache, Boolean> resolverFunc) {
            this.resolverFunc = resolverFunc;
        }

        @Override
        public boolean resolve(SSLContent msgInObj, ByteBufferPool.ByteBufferCache in) {
            return resolverFunc.apply(msgInObj,in);
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

    @Override
    public void log() {

    }
}
