package com.carry.pr.start;

import com.carry.pr.protocol.http.HttpServer;

public class HttpResolver {

    private HttpServer httpServer;

    public HttpResolver(int port) {
        this.httpServer = new HttpServer(port);
    }

    public static void main(String[] args) {
        HttpResolver httpResolver = new HttpResolver(99);
        try {
            httpResolver.httpServer.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
