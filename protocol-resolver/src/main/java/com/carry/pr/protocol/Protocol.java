package com.carry.pr.protocol;

import com.carry.pr.base.executor.WorkGroup;
import com.carry.pr.base.tcp.TcpReadTask;
import com.carry.pr.base.tcp.TcpWorkGroup;
import com.carry.pr.base.tcp.TcpWriteTask;
import com.carry.pr.protocol.http.HttpHandle;
import com.carry.pr.protocol.ssl.SSLHandle;

public enum Protocol {

    TCP(TcpReadTask.TcpDefaultReadHandle.instance, TcpWriteTask.TcpDefaultWriteHandle.instance),

    HTTP(HttpHandle.instance, HttpHandle.instance),

    SSL(SSLHandle.instance, SSLHandle.instance),
    ;
    public final TcpReadTask.ReadHandle readHandle;
    public final TcpWriteTask.WriteHandle writeHandle;

    Protocol(TcpReadTask.ReadHandle readHandle, TcpWriteTask.WriteHandle writeHandle) {
        this.readHandle = readHandle;
        this.writeHandle = writeHandle;
    }

    public WorkGroup createServer(int port) {
        return new TcpWorkGroup(8009).withProtocol(this);
    }

    public TcpReadTask.ReadHandle getReadHandle() {
        return readHandle;
    }

    public TcpWriteTask.WriteHandle getWriteHandle() {
        return writeHandle;
    }
}
