package com.carry.pr.base.executor;

public class DefaultTask implements Task {

    Runnable runnable;

    public DefaultTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public Task next(Worker worker) {
        return null;
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public Task exception() {
        return null;
    }
}
