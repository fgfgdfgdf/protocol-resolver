package com.carry.pr.base;

import java.util.concurrent.ThreadFactory;

public class ResolverThreadFactory implements ThreadFactory {

    public static int incrID = 0;
    public static ResolverThreadFactory threadFactory = new ResolverThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        ResolverThread resolverThread = new ResolverThread(r);
        resolverThread.setName("Resolver Thread -" + (++incrID));
        resolverThread.setDaemon(false);
        return resolverThread;
    }

    public static class ResolverThread extends Thread {
        public ResolverThread(Runnable target) {
            super(target);
        }
    }
}
