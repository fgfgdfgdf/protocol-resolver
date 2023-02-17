package com.carry.pr.base.agent;


import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.resolve.ResolverChannel;

public interface IAgent<T extends ResolverChannel> {

    T getChannel();

    ByteBufferPool.ByteBufferCache forward(ByteBufferPool.ByteBufferCache in);


}
