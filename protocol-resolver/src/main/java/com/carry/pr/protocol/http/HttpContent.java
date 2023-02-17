package com.carry.pr.protocol.http;

import com.carry.pr.base.bytes.OriginalData;
import com.carry.pr.base.resolve.MsgReqObj;
import com.carry.pr.base.resolve.MsgRespObj;

public class HttpContent implements MsgReqObj, MsgRespObj, OriginalData {

    private byte[] original;

    HttpRequest request;
    HttpResponse response;

    HttpResolver resolver;

    @Override
    public byte[] original() {
        return original;
    }


}
