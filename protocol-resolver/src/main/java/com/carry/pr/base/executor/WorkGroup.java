package com.carry.pr.base.executor;

import com.carry.pr.protocol.Protocol;

import java.util.concurrent.Executor;

public abstract class WorkGroup implements Executor {

    protected WorkerFactory workerFactory;
    protected Protocol protocol;

    protected WorkGroup(WorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
    }

    public abstract Worker nextWorker();

    public abstract void start();

    public WorkGroup withProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    @Override
    public void execute(Runnable command) {
        nextWorker().execute(command);
    }
}
