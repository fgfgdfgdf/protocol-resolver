package com.carry.pr.base.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class TheUnsafe {

    private static Unsafe unsafe;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static Unsafe get() {
        return unsafe;
    }
}
