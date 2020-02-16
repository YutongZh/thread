package com.yt.base.automic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntDemo {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10); //初始化值为10
        System.out.println(atomicInteger);
        System.out.println(atomicInteger.getAndIncrement()); //先获取再累加
        System.out.println(atomicInteger.incrementAndGet()); //先累加再获取
        System.out.println(atomicInteger.get());
    }
}
