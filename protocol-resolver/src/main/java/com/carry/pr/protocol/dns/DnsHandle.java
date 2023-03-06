package com.carry.pr.protocol.dns;

import com.carry.pr.base.resolve.AbstractProtocolHandle;
import com.carry.pr.base.resolve.MsgResolver;
import com.carry.pr.base.tcp.TaskContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class DnsHandle extends AbstractProtocolHandle<DnsMessage, DnsMessage> {
    private static final Logger log = LoggerFactory.getLogger(DnsHandle.class);

    public static final DnsHandle instance = new DnsHandle();

    public static final HashMap<String, String> dnsMap = new HashMap<>();

    @Override
    public boolean rhandle(TaskContent content) {
        return getOrCreateInObj(content).decode(content.getIn());
    }

    @Override
    public boolean whandle(TaskContent content) {
        return false;
    }


    @Override
    public MsgResolver<DnsMessage, DnsMessage> getResolver(TaskContent content) {
        return null;
    }

    @Override
    public MsgResolver<DnsMessage, DnsMessage> createResolver(TaskContent content) {
        return null;
    }

    @Override
    public DnsMessage createInObj() {
        return new DnsMessage();
    }

    @Override
    public DnsMessage createOutObj() {
        return new DnsMessage();
    }

    @Override
    public DnsMessage getInObj(TaskContent content) {
        return content.getObj(DnsMessage.class);
    }

    @Override
    public DnsMessage getOutObj(TaskContent content) {
        return content.getObj(DnsMessage.class);
    }
}
