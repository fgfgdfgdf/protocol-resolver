package com.carry.pr.base.client;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketClient {

    private static ConcurrentHashMap<String, Socket> socketMap = new ConcurrentHashMap<>();

    public static Socket getOrCreateTcpSocket(String host, int port) {
        Socket socket = null;
        try {
            socket = socketMap.get(host + ":" + port);
            if (socket != null && !socket.isClosed()) {
                return socket;
            }
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return socket;
    }
}
