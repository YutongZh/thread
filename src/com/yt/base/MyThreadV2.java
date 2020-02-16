package com.yt.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 实现多线程的三种方式
 */
public class MyThreadV2 {

    /**
     * java 单继承 多实现
     */
    public static class ThreadExt extends Thread{
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println("I am ThreadExt " + name);
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //不带返回值
    public static class RunableImpl implements Runnable{
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println("I am RunableImpl " + name);
        }
    }

    //带返回值，通过get获取
    public static class CallableImpl implements Callable<String>{
        @Override
        public String call() throws Exception {
            String name = Thread.currentThread().getName();
            System.out.println("I am CallableImpl " + name);
            return name;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadExt threadExt = new ThreadExt();
        threadExt.start();
        System.out.println("run...");

//        RunableImpl runable = new RunableImpl();
//        new Thread(runable).start();
//
//        CallableImpl callable = new CallableImpl();
//        FutureTask<String> futureTask = new FutureTask(callable);
//        new Thread(futureTask).start();
//        String result = futureTask.get();
//        System.out.println("通过callable获取到的结果是：" + result);
//
//        String name = Thread.currentThread().getName();
//        System.out.println(name);
    }
}
