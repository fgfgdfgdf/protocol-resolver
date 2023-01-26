package com.carry.pr.tcp;

import com.carry.pr.base.Task;
import com.carry.pr.base.Worker;

public class TcpTask implements Task {

    Worker worker;

    @Override
    public Task next() {
        return null;
    }

    @Override
    public void bindWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void run() {

    }
}
