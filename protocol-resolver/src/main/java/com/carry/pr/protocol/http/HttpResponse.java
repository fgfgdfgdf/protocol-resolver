package com.carry.pr.protocol.http;

import com.carry.pr.base.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse extends Response {

    // head
    protected String httpVersion;
    protected int code;
    protected String desc;

    // heads
    protected Map<String, String> head=new HashMap<>();

    // body
    byte[] body;

    public HttpResponse() {
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
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
            sb.append(": ");
            sb.append(v);
            sb.append("\r\n");
        });
        sb.append("\r\n");

        for (byte b : body) {
            sb.append(b);
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }


}