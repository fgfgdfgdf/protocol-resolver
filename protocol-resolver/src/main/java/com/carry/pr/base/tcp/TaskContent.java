package com.carry.pr.base.tcp;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.executor.Worker;
import com.carry.pr.protocol.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskContent {
    private static final AtomicInteger incrId = new AtomicInteger();

    private final int id;

    protected ByteBufferPool.ByteBufferCache in;
    protected ByteBufferPool.ByteBufferCache out;

    Protocol protocol;

    Worker worker;

    TcpChannel tcpChannel;

    private List<Object> objs;

    public TaskContent(TcpChannel tcpChannel, Worker worker) {
        this.tcpChannel = tcpChannel;
        id = incrId.incrementAndGet();
        this.worker = worker;
    }

    public ByteBufferPool.ByteBufferCache getIn() {
        return in;
    }

    public void setIn(ByteBufferPool.ByteBufferCache in) {
        this.in = in;
    }

    public ByteBufferPool.ByteBufferCache getOut() {
        return out;
    }

    public void setOut(ByteBufferPool.ByteBufferCache out) {
        this.out = out;
    }

    public int getId() {
        return id;
    }

    public List<Object> getObjList() {
        if (objs == null)
            objs = new ArrayList<>();
        return objs;
    }

    public <T> T getObj(Class<T> clazz) {
        if (objs == null) return null;
        for (Object o : objs) {
            if (o.getClass().equals(clazz)) {
                return (T) o;
            }
        }
        return null;
    }

    public void putObj(Object obj) {
        getObjList().add(obj);
    }
}
