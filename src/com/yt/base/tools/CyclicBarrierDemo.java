package com.yt.base.tools;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 栅栏  需求：4个EXCEL表单 分别启用4个线程进行银行流水的计算日均流水，最后统计总和，提交表单
 */
public class CyclicBarrierDemo implements Runnable{

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new AMethod());
    //定义线程数为4
    private Executor executorPool = Executors.newFixedThreadPool(4);
    //定义容器，记录每个线程的值
    private Map<String, Integer> resultMap = new ConcurrentHashMap();


    @Override
    public void run() {
        int result = 0;
        if (!resultMap.isEmpty()){
            for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
                System.out.println(entry.getKey() + "======" + entry.getValue());
                result += entry.getValue();
            }
        }
        System.out.println("最终结果========" + result);
    }


    /**
     * 屏障开放 执行的方法
     */
    static class AMethod implements Runnable{
        @Override
        public void run() {
            System.out.println("统计总和操作");
    }
}

    private void count(){
        for (int i = 0; i < 4; i++) {
            executorPool.execute(new Runnable() {
                @Override
                public void run() {
                    //执行计算方法
                    try {
                        //假定计算结果都为1
                        TimeUnit.MILLISECONDS.sleep(100L);
                        System.out.println(Thread.currentThread().getName() + "线程已开放...");
                        cyclicBarrier.await();
                        System.out.println("开始加入");
                        resultMap.put(Thread.currentThread().getName(), 1);
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrierDemo cyclicBarrierDemo = new CyclicBarrierDemo();
        cyclicBarrierDemo.count();
        TimeUnit.MILLISECONDS.sleep(5000L);
        new Thread(cyclicBarrierDemo).start();
    }
}
