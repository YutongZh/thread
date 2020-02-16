package com.yt.base.tools;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 闭锁
 */
public class CountDownLatchDemo {

    //计数器； 并不是线程数
    public static CountDownLatch countDownLatch = new CountDownLatch(3);

    static class CountDownRunner implements Runnable{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "CountDownRunner 执行了");
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "线程执行了");
                countDownLatch.countDown();
            }
        });

        //等待5s自动跳过
        countDownLatch.await(5000L,TimeUnit.MILLISECONDS);

        for (int i = 0; i < 4; i++) {
            new Thread(new CountDownRunner()).start();
        }
        countDownLatch.await();
        System.out.println("main  end....");
    }
}
