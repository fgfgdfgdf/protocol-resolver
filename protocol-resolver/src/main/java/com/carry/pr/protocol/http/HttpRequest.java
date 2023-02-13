package com.carry.pr.protocol.http;

import com.carry.pr.base.bytes.ByteBufferPool;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.carry.pr.protocol.http.HttpRequest.State.*;

public class HttpRequest {
    // 回车
    public static final byte CR = 13;
    // 换行
    public static final byte LF = 10;
    // 空格
    public static final byte SPACE = 32;

    private static final byte[] FLAG_SPACE = {SPACE};
    private static final byte[] FLAG_CRLF = {CR, LF};

    private State state = HEADLINE;

    // line
    protected String method;
    protected String url;
    protected String httpVersion;

    // heads
    protected Map<String, String> heads;

    // bodys
    protected byte[] bodys;

    public HttpRequest() {
        heads = new HashMap<>();
    }

    enum State {
        HEADLINE,
        HEADS,
        DATAS
    }

    public int find(ByteBuffer byteBuffer, int offset, byte[] flag) {
        int index = -1;
        try {
            while (true) {
                if (byteBuffer.get(offset) == flag[0]) {
                    boolean over = true;
                    for (int i = 1; i < flag.length; i++) {
                        if (flag[i] != byteBuffer.get(offset + i)) {
                            over = false;
                            break;
                        }
                    }
                    if (over) {
                        index = offset;
                        break;
                    }
                }
                offset++;
            }
        } catch (IndexOutOfBoundsException e) {
            return index;
        }
        return index;
    }

    public boolean init(ByteBufferPool.ByteBufferCache cache) {
        ByteBuffer byteBuffer = cache.getByteBuffer().duplicate();
        byteBuffer.flip();
        int index;
        if (state == HEADLINE) {
            // headline
            index = find(byteBuffer, cache.getrIndex(), FLAG_CRLF);
            if (index < 0) return false;
            byte[] headLineBytes = new byte[index - cache.getrIndex()];
            byteBuffer.get(headLineBytes);
            String headLineStr = new String(headLineBytes, StandardCharsets.ISO_8859_1);
            String[] headLineAry = headLineStr.split(" ");
            this.method = headLineAry[0].trim();
            this.url = headLineAry[1].trim();
            this.httpVersion = headLineAry[2].trim();
            cache.setrIndex(index + FLAG_CRLF.length);
            state = HEADS;
            byteBuffer.position(cache.getrIndex());
        }


        if (state == HEADS) {
            // heads
            byte[] heads;
            String headStr;
            while (true) {
                index = find(byteBuffer, cache.getrIndex(), FLAG_CRLF);
                if (index == cache.getrIndex()) {
                    cache.setrIndex(index + FLAG_CRLF.length);
                    byteBuffer.position(cache.getrIndex());
                    break;
                }
                if (index < 0) return false;
                heads = new byte[index - cache.getrIndex()];
                byteBuffer.get(heads);
                headStr = new String(heads, StandardCharsets.ISO_8859_1);
                String[] headStrAry = headStr.split(": ");
                this.heads.put(headStrAry[0].trim(), headStrAry[1].trim());
                cache.setrIndex(index + FLAG_CRLF.length);
                byteBuffer.position(cache.getrIndex());
            }
            state = DATAS;
        }

        if (state == DATAS) {
            int contentLength = Integer.parseInt(this.heads.getOrDefault("Content-Length", "0"));
            int limit = byteBuffer.limit();
            if (contentLength <= 0) {
                return true;
            }
            if ((limit - byteBuffer.position()) < contentLength) {
                return false;
            }
            this.bodys = new byte[contentLength];
            byteBuffer.get(this.bodys);
        }
        return true;
    }

}
