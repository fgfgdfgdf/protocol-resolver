package com.carry.pr.base.executor;

public interface Task extends Runnable {

    Task next();

    Task exception();

}