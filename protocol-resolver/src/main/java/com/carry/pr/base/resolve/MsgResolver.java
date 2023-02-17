package com.carry.pr.base.resolve;

import com.carry.pr.base.bytes.ByteBufferPool;

import java.nio.ByteBuffer;

public abstract class MsgResolver<req extends MsgReqObj, resp extends MsgRespObj> {

    // 回车
    public static final byte CR = 13;
    // 换行
    public static final byte LF = 10;
    // 空格
    public static final byte SPACE = 32;

    public static final byte[] FLAG_SPACE = {SPACE};
    public static final byte[] FLAG_CRLF = {CR, LF};

    ResolveChain<req> reqResolverChain;

    ResolveChain<resp> respResolverChain;

    public MsgResolver() {

    }

    public ResolveChain<req> getReqResolverChain() {
        return reqResolverChain;
    }

    public void setReqResolverChain(ResolveChain<req> reqResolverChain) {
        this.reqResolverChain = reqResolverChain;
    }

    public ResolveChain<resp> getRespResolverChain() {
        return respResolverChain;
    }

    public void setRespResolverChain(ResolveChain<resp> respResolverChain) {
        this.respResolverChain = respResolverChain;
    }

    public boolean resolveReq(req request, ByteBufferPool.ByteBufferCache cache) {
        if (reqResolverChain == null)
            reqResolverChain = firstReqChain();
        boolean over;
        do {
            over = reqResolverChain.resolve(request, cache);
        } while (over && (reqResolverChain = reqResolverChain.next()) != null);
        return over;
    }

    public boolean resolveResp(resp resp, ByteBufferPool.ByteBufferCache cache) {
        if (respResolverChain == null)
            respResolverChain = firstRespChain();
        boolean over;
        do {
            over = respResolverChain.resolve(resp, cache);
        } while (over && (respResolverChain = respResolverChain.next()) != null);
        return over;
    }

    public abstract ResolveChain<req> firstReqChain();

    public abstract ResolveChain<resp> firstRespChain();

    public static int find(ByteBuffer byteBuffer, int offset, byte[] flag) {
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
}
