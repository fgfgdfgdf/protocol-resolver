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
                doQueueTask();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    };

    public Worker(){}

    protected void init() throws Exception{
        queue = new LinkedBlockingQueue<>();
        thread = belong.getThreadFactory().newThread(runnable);
        thread.start();
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

    @Override
    public void execute(Runnable command) {
        if (command instanceof Task) {
            Task t=(Task)command;
            t.bindWorker(this);
            queue.offer((Task) command);
        }
    }

    public boolean addTask(Task task) {
        return queue.offer(task);
    }

    public boolean hasTask() {
        return queue.size() == 0;
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
