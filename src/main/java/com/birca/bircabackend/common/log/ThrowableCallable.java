package com.birca.bircabackend.common.log;

@FunctionalInterface
public interface ThrowableCallable<V> {

    V call() throws Throwable;
}
