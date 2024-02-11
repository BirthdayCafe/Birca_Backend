package com.birca.bircabackend.common.log;

@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Throwable;
}
