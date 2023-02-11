package com.carry.pr.base.task;


import com.carry.pr.base.executor.Worker;

public interface Task extends Runnable {

    Task next(Worker worker);

    Task exception();

}