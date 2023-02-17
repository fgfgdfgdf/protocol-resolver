package com.carry.pr.protocol;

import com.carry.pr.base.executor.WorkGroup;
import com.carry.pr.base.tcp.TcpReadTask;
import com.carry.pr.base.tcp.TcpWorkGroup;
import com.carry.pr.base.tcp.TcpWriteTask;
import com.carry.pr.protocol.http.HttpHandle;
import com.carry.pr.protocol.dns.DnsHandle;
import com.carry.pr.protocol.ssl.SSLHandle;

public enum Protocol {

    TCP(TcpReadTask.TcpDefaultReadHandle.instance, TcpWriteTask.TcpDefaultWriteHandle.instance),

    HTTP(80, HttpHandle.instance, HttpHandle.instance),

    SSL(443, SSLHandle.instance, SSLHandle.instance),

    DNS(53, DnsHandle.instance, DnsHandle.instance),
    ;

    public int port = 0;
    public final TcpReadTask.ReadHandle readHandle;
    public final TcpWriteTask.WriteHandle writeHandle;

    Protocol(TcpReadTask.ReadHandle readHandle, TcpWriteTask.WriteHandle writeHandle) {
        this.readHandle = readHandle;
        this.writeHandle = writeHandle;
    }

    Protocol(int port, TcpReadTask.ReadHandle readHandle, TcpWriteTask.WriteHandle writeHandle) {
        this.readHandle = readHandle;
        this.writeHandle = writeHandle;
        this.port = port;
    }

    public WorkGroup createServer(int port) {
        return new TcpWorkGroup(8009).withProtocol(this);
    }

    public WorkGroup createServer() {
        if (this.port == 0)
            throw new RuntimeException("port is 0");
        return new TcpWorkGroup(this.port).withProtocol(this);
    }

    public TcpReadTask.ReadHandle getReadHandle() {
        return readHandle;
    }

    public TcpWriteTask.WriteHandle getWriteHandle() {
        return writeHandle;
    }
}
