package com.carry.pr.base.executor;

import java.util.concurrent.Executor;

public abstract class WorkGroup implements Executor {

    protected WorkerFactory workerFactory;

    protected WorkGroup(WorkerFactory workerFactory) {
        this.workerFactory = workerFactory;
    }

    public abstract Worker nextWorker();

    public abstract void start();

    @Override
    public void execute(Runnable command) {
        nextWorker().execute(command);
    }
}
