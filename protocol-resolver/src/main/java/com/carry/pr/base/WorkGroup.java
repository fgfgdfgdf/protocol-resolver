package com.carry.pr.base;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@SuppressWarnings("all")
public class WorkGroup implements Executor {

    private int incrId = 0;
    private Worker[] workers;
    ThreadFactory threadFactory;

    public WorkGroup(Class<? extends Worker> clazz) {
        this(Runtime.getRuntime().availableProcessors() * 2, clazz);
    }

    public WorkGroup(int threads, Class<? extends Worker> clazz) {
        this.threadFactory = ResolverThreadFactory.threadFactory;
        workers = new Worker[threads];
        initWorker(clazz);
    }

    public WorkGroup(Worker[] worker) {
        this.threadFactory = ResolverThreadFactory.threadFactory;
        workers = worker;
    }

    private void initWorker(Class<? extends Worker> clazz) {
        try {
            for (int i = 0; i < workers.length; i++) {
                workers[i] = clazz.newInstance();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void start() {
        try {
            for (int i = 0; i < workers.length; i++) {
                workers[i].setBelong(this);
                workers[i].start();
            }
        } catch (Throwable t) {
            t.printStackTrace();
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
