package com.carry.pr.base.resolve;

import com.carry.pr.base.bytes.ByteBufferPool;

public interface ResolveChain<T> {

    boolean resolve(T msgInObj, ByteBufferPool.ByteBufferCache cache);

    ResolveChain<T> next();


}
