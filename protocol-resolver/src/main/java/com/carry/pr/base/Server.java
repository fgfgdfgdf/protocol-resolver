package com.carry.pr.base;

public abstract class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

}
