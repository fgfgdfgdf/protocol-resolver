package com.carry.pr.base;

import com.carry.pr.base.impl.ResolverThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@SuppressWarnings("all")
public class WorkGroup implements Executor {

    private int incrId = 0;
    private Worker[] workers;
    ThreadFactory threadFactory;

    public WorkGroup(int threads) {
        this.threadFactory = ResolverThreadFactory.threadFactory;
        workers = new Worker[threads];
    }

    public void init(Class<? extends Worker> clazz) throws Exception {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = clazz.newInstance();
            workers[i].setBelong(this);
            workers[i].init();
        }
    }


    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public Worker nextWorker() {
        return workers[incrId++ % workers.length];
    }

    @Override
    public void execute(Runnable command) {
        nextWorker().execute(command);
    }
}
