package com.carry.pr.base;

public interface Task extends Runnable {
    Task next();
    void bindWorker(Worker worker);
}