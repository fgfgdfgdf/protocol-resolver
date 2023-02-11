package com.carry.pr.base.executor;


import com.carry.pr.base.task.Task;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 默认阻塞执行
 */
public class Worker implements Executor, Runnable {

    private long workCount;
    private final WorkGroup belong;
    private final BlockingQueue<Runnable> queue;

    private Thread thread;

    public Worker(WorkGroup workGroup) {
        belong = workGroup;
        queue = new LinkedBlockingQueue<>();
    }

    public void start() throws IOException {
        thread = belong.workerFactory.newThread(this);
        thread.start();
    }

    public void doQueueTask(boolean blockWait) throws InterruptedException {
        Runnable task;
        if ((task = (blockWait ? queue.take() : queue.poll())) != null) {
            taskRun(task);
            ++workCount;
        }
    }

    public void taskRun(Runnable task) {
        Task next;
        try {
            task.run();
        } catch (Throwable t) {
            t.printStackTrace();
            if ((task instanceof Task) && (next = ((Task) task).exception()) != null) {
                execute(next);
            }
        }
        if ((task instanceof Task) && (next = ((Task) task).next(this)) != null) {
            execute(next);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            try {
                doQueueTask(true);
            } catch (InterruptedException t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) return;
        if (inWorkThread()) {
            taskRun(runnable);
        } else {
            addTask(runnable);
        }
    }

    protected boolean inWorkThread() {
        return thread == Thread.currentThread();
    }

    public WorkGroup getBelong() {
        return belong;
    }

    public boolean addTask(Runnable task) {
        return queue.offer(task);
    }

    public boolean hasTask() {
        return queue.size() > 0;
    }

    public long getWorkCount() {
        return workCount;
    }

    public void interrupt() {
        thread.interrupt();
    }
}
