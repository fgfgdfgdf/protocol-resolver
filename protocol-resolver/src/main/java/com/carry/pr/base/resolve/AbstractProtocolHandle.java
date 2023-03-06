package com.carry.pr.base.resolve;

import com.carry.pr.base.tcp.TaskContent;

public abstract class AbstractProtocolHandle<in extends MsgReqObj, out extends MsgRespObj> implements ProtocolHandle<in, out> {

    public in getOrCreateInObj(TaskContent content) {
        in inObj = getInObj(content);
        if (inObj == null) {
            inObj = createInObj();
            content.putObj(inObj);
        }
        return inObj;
    }

    public MsgResolver<in, out> getOrCreateResolver(TaskContent content) {
        MsgResolver<in, out> resolver = getResolver(content);
        if (resolver == null) {
            resolver = createResolver(content);
            content.putObj(resolver);
        }
        return resolver;
    }

    public abstract MsgResolver<in, out> getResolver(TaskContent content);

    public abstract MsgResolver<in, out> createResolver(TaskContent content);

    public abstract in createInObj();

    public abstract out createOutObj();


}
