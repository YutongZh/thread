package com.yt.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class MapTest {


    public static CountDownLatch countDownLatch = new CountDownLatch(1);

    static class MyRun implements Runnable{

        @Override
        public void run() {
            System.out.println("任务执行了");
        }
    }
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(5);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new MyRun(), 5000, 1000, TimeUnit.MILLISECONDS);
        Object o = scheduledFuture.get();

    }
}
