package com.carry.pr.protocol.http;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.bytes.OriginalData;
import com.carry.pr.base.resolve.MsgReqObj;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest implements MsgReqObj, OriginalData {

    protected String method;
    protected String url;
    protected String httpVersion;
    protected Map<String, String> heads;
    protected byte[] bodys;


    public HttpRequest() {
        heads = new HashMap<>();
    }

    public boolean decode(ByteBufferPool.ByteBufferCache in) {
        return new HttpResolver().resolve(this, in);
    }


    @Override
    public void log() {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append(" ");
        sb.append(url);
        sb.append(" ");
        sb.append(httpVersion);
        sb.append("\r\n");
        heads.forEach((k, v) -> {
            sb.append(k);
            sb.append(":");
            sb.append(v);
            sb.append("\r\n");
        });
        if (bodys != null) {
            for (int i = 0; i < bodys.length; i++) {
                sb.append((char) bodys[i]);
            }
        }
        System.out.println(sb);
    }

    @Override
    public byte[] original() {
        return new byte[0];
    }
}
