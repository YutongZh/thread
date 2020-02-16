package com.yt.base.forkjoin;

public class SingltonSum {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int sum = 0;
        for (int i = 1; i <=100000000; i++) {
            sum += i;
        }
        System.out.println("结果：" + sum + "；执行时间：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
