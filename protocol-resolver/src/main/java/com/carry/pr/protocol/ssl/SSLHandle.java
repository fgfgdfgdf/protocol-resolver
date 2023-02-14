package com.carry.pr.protocol.ssl;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.tcp.TaskContent;
import com.carry.pr.base.tcp.TcpReadTask;
import com.carry.pr.base.tcp.TcpWriteTask;
import com.carry.pr.protocol.http.HttpHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLHandle implements TcpReadTask.ReadHandle, TcpWriteTask.WriteHandle {

    private static final Logger log = LoggerFactory.getLogger(HttpHandle.class);

    public static final SSLHandle instance = new SSLHandle();

    @Override
    public boolean rhandle(TaskContent content) {
        System.out.println(Thread.currentThread().getName());
        SSLContent sslContent = getOrCreate(content);
        ByteBufferPool.ByteBufferCache in = content.getIn();
        return sslContent.read(in);
    }

    @Override
    public boolean whandle(TaskContent content) {
        return false;
    }

    public SSLContent getOrCreate(TaskContent content) {
        SSLContent obj = content.getObj(SSLContent.class);
        if (obj == null) {
            obj = new SSLContent();
            content.putObj(obj);
        }
        return obj;
    }

}
