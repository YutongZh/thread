package com.yt.base.tools;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 *  Future FutureTask callable 用法
 */
public class FutureTaskDemo implements Callable<Integer> {


    @Override
    public Integer call(){
        int sum = 0;
        for (int i = 0; i < 100000; i++) {
            sum += i;
        }
        try{
            while (!Thread.currentThread().isInterrupted()){
                Thread.sleep(1000);
                System.out.println("循环中。。。。");
            }
        }catch (InterruptedException e){
            System.out.println("中断了。。。isInterrupt  flag is  " + Thread.currentThread().isInterrupted());
            Thread.currentThread().interrupt();
        }

        System.out.println("中断状态为" + Thread.currentThread().isInterrupted());


        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTaskDemo futureTaskDemo = new FutureTaskDemo();
        FutureTask<Integer> futureTask = new FutureTask(futureTaskDemo);
        Thread thread = new Thread(futureTask);
        thread.start();

        Random random = new Random();
        if (random.nextBoolean()) {
            System.out.println("计算结果为：：：：" + futureTask.get());
        } else {
            //中断
            TimeUnit.SECONDS.sleep(2);
            boolean cancel = futureTask.cancel(true);
            System.out.println("程序中断了");
        }
    }
}
