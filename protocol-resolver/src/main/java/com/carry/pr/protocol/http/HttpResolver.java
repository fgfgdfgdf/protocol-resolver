package com.carry.pr.protocol.http;

import com.carry.pr.base.bytes.ByteBufferPool;
import com.carry.pr.base.resolve.ResolveChain;
import com.carry.pr.base.resolve.MsgResolver;

import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;


public class HttpResolver extends MsgResolver<HttpRequest, HttpResponse> {

    public HttpResolver() {
    }


    @Override
    public ResolveChain<HttpRequest> firstReqChain() {
        return ReqResolve.values()[0];
    }

    @Override
    public ResolveChain<HttpResponse> firstRespChain() {
        return ResoResolve.values()[0];
    }

    private enum ReqResolve implements ResolveChain<HttpRequest> {
        HEADLINE((request, in) -> {
            int rIndex = in.getrIndex();
            int index = find(in.getByteBuffer(), rIndex, HttpResolver.FLAG_CRLF);
            if (index < 0) return false;
            byte[] headLineBytes = new byte[index - rIndex];
            in.getByteBuffer().get(headLineBytes);
            String headLineStr = new String(headLineBytes, StandardCharsets.ISO_8859_1);
            String[] headLineAry = headLineStr.split(" ");
            request.method = headLineAry[0].trim();
            request.url = headLineAry[1].trim();
            request.httpVersion = headLineAry[2].trim();
            in.setrIndex(index + FLAG_CRLF.length);
            return true;
        }),
        HEADS((request, in) -> {
            byte[] heads;
            String headStr;
            int index;
            while (true) {
                index = find(in.getByteBuffer(), in.getrIndex(), FLAG_CRLF);
                if (index == in.getrIndex()) {
                    in.setrIndex(index + FLAG_CRLF.length);
                    in.getByteBuffer().position(in.getrIndex());
                    break;
                }
                if (index < 0) return false;
                heads = new byte[index - in.getrIndex()];
                in.getByteBuffer().get(heads);
                headStr = new String(heads, StandardCharsets.ISO_8859_1);
                String[] headStrAry = headStr.split(": ");
                request.heads.put(headStrAry[0].trim(), headStrAry[1].trim());
                in.setrIndex(index + FLAG_CRLF.length);
            }
            return true;
        }),
        DATAS((request, in) -> {
            int contentLength = Integer.parseInt(request.heads.getOrDefault("Content-Length", "0"));
            int limit = in.getByteBuffer().limit();
            if (contentLength <= 0) {
                return true;
            }
            if ((limit - in.getrIndex()) < contentLength) {
                return false;
            }
            request.bodys = new byte[contentLength];
            in.getByteBuffer().get(request.bodys);
            return true;
        });

        final BiFunction<HttpRequest, ByteBufferPool.ByteBufferCache, Boolean> resolverFunc;

        ReqResolve(BiFunction<HttpRequest, ByteBufferPool.ByteBufferCache, Boolean> resolverFunc) {
            this.resolverFunc = resolverFunc;
        }

        @Override
        public boolean resolve(HttpRequest msgInObj, ByteBufferPool.ByteBufferCache in) {
            return resolverFunc.apply(msgInObj, in);
        }

        @Override
        public ResolveChain<HttpRequest> next() {
            ReqResolve[] values = values();
            return ordinal() + 1 < values.length ? values[ordinal() + 1] : null;
        }
    }


    private enum ResoResolve implements ResolveChain<HttpResponse> {


        ;

        @Override
        public boolean resolve(HttpResponse msgInObj, ByteBufferPool.ByteBufferCache cache) {
            return false;
        }

        @Override
        public ResolveChain<HttpResponse> next() {
            return null;
        }
    }
}
