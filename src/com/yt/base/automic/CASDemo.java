package com.yt.base.automic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// 用cas实现计数器  线程安全和不安全的对比
public class CASDemo {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private int count = 0;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        CASDemo cas = new CASDemo();
        List<Thread> list = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100000; j++) {
                        cas.safeCount();
                        cas.unSafeCount();
                    }
                }
            });
            thread.start();
            list.add(thread);
        }

        for (Thread t : list) {
            t.join();
        }

        System.out.println("线程不安全的计数器：" + cas.count);
        System.out.println("线程安全的计数器：" + cas.atomicInteger);
        System.out.println("耗时：" + (System.currentTimeMillis() - start) + "毫秒");

    }
    public void safeCount(){
        for (;;){
            int i = atomicInteger.get();
            boolean b = atomicInteger.compareAndSet(i, ++i);
            if (b){
                break;
            }
        }
    }

    public void unSafeCount(){
        count ++;
    }
}
