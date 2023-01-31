package com.carry.pr;

import com.carry.pr.base.executor.WorkGroup;
import com.carry.pr.base.tcp.TcpWorkGroup;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        WorkGroup workGroup = new TcpWorkGroup(8009);
        workGroup.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                String next = scanner.next();
                workGroup.execute(() -> {
                    System.out.println("execute param:" + next);
                });
            }
        }

    }
}
