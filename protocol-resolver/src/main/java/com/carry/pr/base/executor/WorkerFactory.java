package com.carry.pr.base.executor;

import java.util.concurrent.ThreadFactory;

public class WorkerFactory implements ThreadFactory {

    public static int number = 0;
    public static WorkerFactory defaultFactory = new WorkerFactory();

    @Override
    public Thread newThread(Runnable r) {
        WorkerThread resolverThread = new WorkerThread(r);
        resolverThread.setName("Worker Thread -" + (++number));
        return resolverThread;
    }

    public static class WorkerThread extends Thread {
        public WorkerThread(Runnable target) {
            super(target);
        }
    }
}
