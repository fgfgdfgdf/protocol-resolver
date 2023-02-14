package com.carry.pr.protocol.ssl;

import com.carry.pr.base.bytes.ByteBufferPool;

import java.nio.ByteBuffer;

public abstract class HandShakeContent {

    protected int totalLength;

    public byte[] origin;

    public HandShakeContent() {
    }

    protected abstract void decode(ByteBufferPool.ByteBufferCache in);

    protected void recordOrigin(ByteBufferPool.ByteBufferCache in) {
        int rIndex = in.getrIndex();
        ByteBuffer byteBuffer = in.getByteBuffer();
        origin = new byte[totalLength];
        for (int i = rIndex; i < totalLength; i++) {
            origin[i] = byteBuffer.get(i);
        }
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }
}
