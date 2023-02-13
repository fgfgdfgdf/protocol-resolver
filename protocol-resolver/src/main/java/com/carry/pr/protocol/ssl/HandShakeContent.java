package com.carry.pr.protocol.ssl;

import com.carry.pr.base.bytes.ByteBufferPool;

public abstract class HandShakeContent {

    public HandShakeContent() {
    }

    protected abstract void decode(ByteBufferPool.ByteBufferCache in);
}
