package com.carry.pr;

import com.carry.pr.base.executor.WorkGroup;
import com.carry.pr.protocol.Protocol;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        WorkGroup workGroup = Protocol.HTTP.createServer(8009);
        workGroup.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                String next = scanner.next();
                workGroup.execute(() -> {
                    System.out.println(Thread.currentThread().getName()+",execute param:" + next);
                });
            }
        }

    }
}
