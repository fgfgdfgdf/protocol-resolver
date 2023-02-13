package com.carry.pr.base.common;

public class ResolverChain<T> {

    T data;

    ResolverChain<T> next;

    public ResolverChain(T data, ResolverChain<T> next) {
        this.data = data;
        this.next = next;
    }

    public boolean doResolver() {

        return true;
    }

}
