package com.carry.pr.protocol.http;

import com.carry.pr.base.resolve.MsgRespObj;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse implements MsgRespObj {

    protected String httpVersion;
    protected int code;
    protected String desc;
    protected Map<String, String> head = new HashMap<>();
    byte[] body;

    public HttpResponse() {
    }

    public void write(int b) throws IOException {
    }


    public byte[] getBytes() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpVersion);
        sb.append(" ");
        sb.append(code);
        sb.append(" ");
        sb.append(desc);
        sb.append("\r\n");
        head.forEach((k, v) -> {
            sb.append(k);
            sb.append(":");
            sb.append(v);
            sb.append("\r\n");
        });
        sb.append("\r\n");
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        if (body != null) {
            byte[] newBytes = new byte[bytes.length + bytes.length];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            System.arraycopy(body, 0, newBytes, bytes.length, body.length);
            return newBytes;
        }
        return bytes;
    }


    public static HttpResponse defaultResp(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.httpVersion = httpRequest.httpVersion;
        httpResponse.code = 200;
        httpResponse.desc = "OK";
        Map<String, String> heads = httpResponse.head;

//        heads.put("Server", "protocol-resolver");

        httpResponse.body = ("hello,date:" + new Date() + ",from protocol-resolver").getBytes(StandardCharsets.UTF_8);
        heads.put("content-type", "text/plain");
        heads.put("content-length", String.valueOf(httpResponse.body.length));
        return httpResponse;
    }

}
