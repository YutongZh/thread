package com.yt.base.tools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 两个线程之间的交换信息 第一个exchanger会阻塞，直到执行第二个exchanger位置
 */
public class ExchagerTest{

    static Exchanger<String> exchanger = new Exchanger<>();

    public static Executor executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String A = "银行流水A";
                try {
                    exchanger.exchange(A);
                    System.out.println("A 执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                String B = "银行流水B";
                try {
                    String A = exchanger.exchange(B);
                    System.out.println("B执行完毕...");
                    System.out.println("A=" + A + " B= " + B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
