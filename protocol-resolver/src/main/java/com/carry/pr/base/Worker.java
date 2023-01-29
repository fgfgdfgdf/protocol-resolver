package com.carry.pr.base;


import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class Worker implements Executor {

    WorkGroup belong;
    BlockingQueue<Task> queue;
    Thread thread;

    Runnable runnable = () -> {
        while (true) {
            try {
                work();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    };

    public Worker() {
        queue = new LinkedBlockingQueue<>();
    }

    protected void start() throws Exception {
        thread = belong.getThreadFactory().newThread(runnable);
        thread.start();
    }

    public void work() {
        doQueueTask();
    }

    public void doQueueTask() {
        Task task;
        if ((task = queue.peek()) != null) {
            task.run();
            queue.poll();
            if ((task = task.next()) != null) {
                execute(task);
            }
        }
    }

    public static class DefaultTask implements Task {
        Runnable runnable;

        public DefaultTask(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public Task next() {
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

    @Override
    public void execute(Runnable command) {
        Task t;
        if (command instanceof Task) {
            t = (Task) command;
        } else {
            t = new DefaultTask(command);
        }
        addTask(t);
    }

    protected boolean addTask(Task task) {
        return queue.offer(task);
    }

    public boolean hasTask() {
        return queue.size() > 0;
    }

    public Queue<Task> getQueue() {
        return queue;
    }

    public WorkGroup getGroup() {
        return belong;
    }

    public void setBelong(WorkGroup belong) {
        this.belong = belong;
    }
}
