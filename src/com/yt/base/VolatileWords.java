package com.yt.base;

import java.util.concurrent.TimeUnit;

/**
 * volatile 保证可见性 但不保证原子性
 */
public class VolatileWords {

    public static class VolatileValue implements Runnable{
        private volatile int a;
        @Override
        public void run() {
            try {
                a = a + 1;
                System.out.println(Thread.currentThread().getName() + "====" + a);
                TimeUnit.MILLISECONDS.sleep(50);
                a = a + 1;
                System.out.println(Thread.currentThread().getName() + "====" + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        VolatileValue volatileValue = new VolatileValue();
        for (int i = 0; i < 100; i++) {
            new Thread(volatileValue).start();
        }
    }
}
