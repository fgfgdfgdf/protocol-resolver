package com.carry.pr.protocol.ssl;

import com.carry.pr.base.resolve.AbstractProtocolHandle;
import com.carry.pr.base.resolve.MsgResolver;
import com.carry.pr.base.tcp.TaskContent;
import com.carry.pr.protocol.http.HttpHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLHandle extends AbstractProtocolHandle<SSLContent, SSLContent> {

    private static final Logger log = LoggerFactory.getLogger(HttpHandle.class);
    public static final SSLHandle instance = new SSLHandle();

    @Override
    public boolean rhandle(TaskContent content) {
        return getOrCreateInObj(content).read(content.getIn());
    }

    @Override
    public boolean whandle(TaskContent content) {
        return false;
    }

    @Override
    public MsgResolver<SSLContent, SSLContent> getResolver(TaskContent content) {
        return null;
    }

    @Override
    public MsgResolver<SSLContent, SSLContent> createResolver(TaskContent content) {
        return null;
    }

    @Override
    public SSLContent createInObj() {
        return new SSLContent();
    }

    @Override
    public SSLContent createOutObj() {
        return new SSLContent();
    }

    @Override
    public SSLContent getInObj(TaskContent content) {
        return content.getObj(SSLContent.class);
    }

    @Override
    public SSLContent getOutObj(TaskContent content) {
        return content.getObj(SSLContent.class);
    }
}
