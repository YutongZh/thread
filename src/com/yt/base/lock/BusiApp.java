package com.yt.base.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BusiApp {

    //读写比
    static final int READ_WRITE_RATIO = 10;
    //最少线程数
    static final int MIN_THREAD_NUM = 3;

    //读线程
    public static class ReadThread implements Runnable{
        private GoodsService goodsService;

        public ReadThread(GoodsService goodsService){
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) { //操作100次
                try {
                    goodsService.getNum();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "读线程操作的时间为：" + (System.currentTimeMillis() - start) + "毫秒");
        }
    }

    //写线程
    public static class WriteRead implements Runnable {
        private GoodsService goodsService;

        public WriteRead(GoodsService goodsService){
            this.goodsService = goodsService;
        }
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    goodsService.setNum(random.nextInt());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "写线程操作的时间为：" + (System.currentTimeMillis() - start) + "毫秒");
        }
    }

    /**
     *  synchronized 和 读写锁性能对比 （读多写少情况下） 差距很大
     */
    public static void main(String[] args) throws InterruptedException {
        GoodsInfo goodsInfo = new GoodsInfo("小米", 100);
        //GoodsService goodsService = new SynLock(goodsInfo);
        GoodsService goodsService = new MyReadWriteLock(goodsInfo);
        for (int i = 0; i < MIN_THREAD_NUM; i++) {
            Thread writeThread = new Thread(new WriteRead(goodsService));
            for (int j = 0; j < READ_WRITE_RATIO; j++) {
                Thread readThread = new Thread(new ReadThread(goodsService));
                readThread.start();
            }
            TimeUnit.MILLISECONDS.sleep(100);
            writeThread.start();
        }
    }

}
