package com.carry.pr.base.common;


import com.carry.pr.base.tcp.TaskContent;
import com.carry.pr.base.tcp.TcpReadTask;
import com.carry.pr.base.tcp.TcpWriteTask;

public abstract class DataHandle<T> implements StageOperation, TcpReadTask.ReadHandle, TcpWriteTask.WriteHandle {

    int stage;
    int rIndex;
    int wIndex;

    @Override
    public void nextStage() {
        stage++;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean rhandle(TaskContent content) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean whandle(TaskContent content) {
        throw new UnsupportedOperationException();
    }
}
