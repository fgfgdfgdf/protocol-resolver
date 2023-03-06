package com.carry.pr.protocol.http;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.resolve.AbstractProtocolHandle;
import com.carry.pr.base.resolve.MsgResolver;
import com.carry.pr.base.tcp.TaskContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpHandle extends AbstractProtocolHandle<HttpRequest, HttpResponse> {

    private static final Logger log = LoggerFactory.getLogger(HttpHandle.class);
    public static final HttpHandle instance = new HttpHandle();

    @Override
    public boolean rhandle(TaskContent content) {
        MsgResolver<HttpRequest, HttpResponse> resolver = getOrCreateResolver(content);
        HttpRequest request = getOrCreateInObj(content);
        return resolver.resolveReq(request, content.getIn());
    }

    @Override
    public boolean whandle(TaskContent content) {
        HttpRequest requset = getInObj(content);
        requset.log();
        byte[] bytes = HttpResponse.defaultResp(requset).getBytes();
        content.setOut(ByteBufferPool.optimalBytes(content, bytes));
        return true;
    }

    @Override
    public MsgResolver<HttpRequest, HttpResponse> createResolver(TaskContent content) {
        return new HttpResolver();
    }

    @Override
    public MsgResolver<HttpRequest, HttpResponse> getResolver(TaskContent content) {
        return content.getObj(HttpResolver.class);
    }

    @Override
    public HttpRequest createInObj() {
        return new HttpRequest();
    }

    @Override
    public HttpResponse createOutObj() {
        return new HttpResponse();
    }

    @Override
    public HttpRequest getInObj(TaskContent content) {
        return content.getObj(HttpRequest.class);
    }

    @Override
    public HttpResponse getOutObj(TaskContent content) {
        return content.getObj(HttpResponse.class);
    }
}
