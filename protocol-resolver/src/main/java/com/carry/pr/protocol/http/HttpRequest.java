package com.carry.pr.protocol.http;

import com.carry.pr.base.Request;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends Request {
    // 回车
    public static final byte CR = 13;
    // 换行
    public static final byte LF = 10;
    public static final byte SPACE = 32;


    // line
    protected String method;
    protected String url;
    protected String httpVersion;

    // heads
    protected Map<String, String> heads;

    // bodys
    protected byte[] bodys;

    private HttpRequest() {
    }

    public static HttpRequest init(ByteBuffer data) throws Exception {
        HttpRequest request = new HttpRequest();
        int readindex = 0;
        int preRead = 0;
        while (true) {
            preRead++;
            byte b = data.get();
            if (b == LF) break;
        }
        // 请求行
        byte[] headLine = new byte[preRead];
        for (int i = 0; i < preRead; i++) {
            headLine[i] = data.get(i);
        }
        readindex = preRead;
        String headlineStr = new String(headLine, StandardCharsets.ISO_8859_1);
        headlineStr = headlineStr.replace("\r\n", "");
        String[] split = headlineStr.split(" ");
        request.method = split[0];
        request.url = split[1];
        request.httpVersion = split[2];


        // 请求头
        int bodyLength = 0;
        byte end = 0;
        while (true) {
            preRead++;
            byte b = data.get();
            if (b == LF || b == CR) {
                end++;
            } else {
                end = 0;
            }
            if (end >= 4) break;
        }
        byte[] heads = new byte[preRead - readindex];
        for (int i = 0; i < preRead - readindex; i++) {
            heads[i] = data.get(readindex+i);
        }
        String headStr = new String(heads, StandardCharsets.ISO_8859_1);
        String[] headStrSp = headStr.split("\r\n");
        Map<String, String> map = new HashMap<>();
        for (String head : headStrSp) {
            if (head.equals("\r\n")) continue;
            String[] kv = head.split(":");
            map.put(kv[0], kv[1]);
            if("Content-Length".equals(kv[0])){
                bodyLength=Integer.parseInt(kv[1].trim());
            }
        }
        request.heads = map;


        // 请求body
        byte[] body = new byte[bodyLength];
        data.get(body, 0, bodyLength);
        request.bodys = body;

        return request;
    }

}
