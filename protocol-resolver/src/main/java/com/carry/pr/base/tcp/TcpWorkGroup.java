package com.carry.pr.base.tcp;

import com.carry.pr.base.executor.WorkGroup;
import com.carry.pr.base.executor.Worker;
import com.carry.pr.base.executor.WorkerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class TcpWorkGroup extends WorkGroup {
    private final AtomicInteger incr;

    private final Worker accecpter;
    private final Worker[] workers;

    public TcpWorkGroup(int port) {
        this(port, Runtime.getRuntime().availableProcessors(), WorkerFactory.defaultFactory);
    }

    public TcpWorkGroup(int port, int workerNum, WorkerFactory workerFactory) {
        super(workerFactory);
        incr = new AtomicInteger();
        workers = new Worker[workerNum];
        for (int i = 0; i < workerNum; i++) {
            workers[i] = new TcpWorker(this);
        }
        accecpter = new TcpAcceptWorker(port, this, this);
    }

    @Override
    public void start() {
        try {
            for (Worker worker : workers) {
                worker.start();
            }
            accecpter.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public Worker nextWorker() {
        return workers[incr.getAndIncrement() % workers.length];
    }
}
