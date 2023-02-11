package com.carry.pr.protocol.http;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.tcp.TaskContent;
import com.carry.pr.base.tcp.TcpReadTask;
import com.carry.pr.base.tcp.TcpWriteTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;


public class HttpHandle implements TcpReadTask.ReadHandle, TcpWriteTask.WriteHandle {
    private static final Logger log = LoggerFactory.getLogger(HttpHandle.class);

    public static final HttpHandle instance = new HttpHandle();


    @Override
    public boolean rhandle(TaskContent content) {
        ByteBufferPool.ByteBufferCache in = content.getIn();
        HttpRequest httpRequest = content.getObj(HttpRequest.class);
        if (httpRequest == null) {
            httpRequest = new HttpRequest();
            content.putObj(httpRequest);
        }
        return httpRequest.init(in);
    }

    @Override
    public boolean whandle(TaskContent content) {
        ByteBufferPool.ByteBufferCache in = content.getOut();
        HttpRequest httpRequest = content.getObj(HttpRequest.class);
        HttpResponse httpResponse = HttpResponse.defaultResp(httpRequest);
        byte[] bytes = httpResponse.getBytes();
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        System.out.println();
        ByteBufferPool.ByteBufferCache cache = ByteBufferPool.optimalSize(content, bytes.length);
        cache.getByteBuffer().put(bytes);
        content.setOut(cache);
        return true;
    }
}
