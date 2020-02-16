package com.yt.base;

import com.apple.eawt.PreferencesHandler;

import java.util.concurrent.TimeUnit;

/**
 * join的用法
 */
public class ThreadJoin {

    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runner(previous), "Child Thread  00" + i);
            System.out.println(previous.getName() + "  插入到线程 " + thread.getName() + "  之前执行...");
            thread.start();
            previous = thread;
        }

        TimeUnit.SECONDS.sleep(2L);
        System.out.println("主线程：" + Thread.currentThread() + "  执行完毕...");
    }


    static class Runner implements Runnable{
        private Thread thread;

        public Runner(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程" + Thread.currentThread() + "  执行完毕！");
        }
    }
}
