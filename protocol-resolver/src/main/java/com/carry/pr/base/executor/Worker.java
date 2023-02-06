package com.carry.pr.base.executor;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Executor, Runnable {

    private long workCount;
    private final WorkGroup belong;
    private final BlockingQueue<Task> queue;
    private Thread thread;

    private AtomicBoolean working;

    public Worker(WorkGroup workGroup) {
        belong = workGroup;
        queue = new LinkedBlockingQueue<>();
        working = new AtomicBoolean();
    }

    public void start() throws Exception {
        if (working.get()) return;
        thread = belong.workerFactory.newThread(this);
        thread.start();
        working.compareAndSet(false, true);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void work() {
        while (true) {
            try {
                doQueueTask(true);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void doQueueTask(boolean blockWait) throws InterruptedException {
        Task task;
        if ((task = (blockWait ? queue.take() : queue.peek())) != null) {
            if (!blockWait)
                queue.poll();
            try {
                task.run();
            } catch (Throwable t) {
                t.printStackTrace();
                if ((task = task.exception()) != null) {
                    execute(task);
                }
            }
            assert task != null;
            if ((task = task.next(this)) != null) {
                execute(task);
            }
            ++workCount;
        }
    }

    @Override
    public final void run() {
        work();
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) return;
        Task t;
        if (command instanceof Task) {
            t = (Task) command;
        } else {
            t = new DefaultTask(command);
        }
        addTask(t);
    }

    public boolean addTask(Task task) {
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
