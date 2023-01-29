package com.carry.pr.base;

public interface Task extends Runnable {

    Task next();

    Task exception();

}