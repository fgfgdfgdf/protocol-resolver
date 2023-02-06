package com.carry.pr.base.tcp;

import java.nio.channels.Selector;

public class SelectorProvider {

    public static Selector open() {
        Selector selector = null;
        try {
            java.nio.channels.spi.SelectorProvider provider = java.nio.channels.spi.SelectorProvider.provider();
            selector = provider.openSelector();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        try {
//            Class<?> aClass = Class.forName("sun.nio.ch.SelectorImpl", false, Thread.currentThread().getContextClassLoader());
//            Class<?> superclass = aClass.getSuperclass();
//            Field selectedKeys = aClass.getDeclaredField("selectedKeys");
//            Field publicSelectedKeys = aClass.getDeclaredField("publicSelectedKeys");
//            selectedKeys.setAccessible(true);
//            publicSelectedKeys.setAccessible(true);
//            SelectedSelectionKeySet replac = new SelectedSelectionKeySet();
//            selectedKeys.set(selector, replac);
//            publicSelectedKeys.set(selector, replac);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return selector;
    }
}
