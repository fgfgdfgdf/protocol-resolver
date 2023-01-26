package com.carry.pr.tcp;


import com.carry.pr.base.Worker;

import java.nio.channels.Selector;

public class TcpWorker extends Worker {

    Selector selector;


    public TcpWorker() {
    }

    @Override
    protected void init() throws Exception {
        super.init();
        this.selector = Selector.open();
    }




}
