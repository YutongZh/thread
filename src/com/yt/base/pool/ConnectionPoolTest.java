package com.yt.base.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

//手写线程池
public class ConnectionPoolTest {

    //初始化连接池 10个连接
    static ConnectionPool pool = new ConnectionPool(10);
    //保证所有的connectRunner能够同时进行
    static CountDownLatch start = new CountDownLatch(1);
    //控制所有的word线程结束后才开始执行
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        //线程数量
        Integer threadNum = 50;
        end = new CountDownLatch(threadNum);
        //20次访问线程池操作
        Integer count = 20;
        AtomicInteger getCount = new AtomicInteger();
        AtomicInteger notGetCount = new AtomicInteger();
        for (int i = 0; i < threadNum; i++) {
            new Thread(new ConnectionRunner(count, getCount, notGetCount)).start();
        }
        //保证线程同一个时刻运行
        start.countDown();
        //主线程等待 所有的子线程执行完毕才计数
        end.await();
        System.out.println("总共的调用次数为：" + threadNum * count);
        System.out.println("共计：" + getCount + "获取成功");
        System.out.println("共计：" + notGetCount + "获取失败");
    }

    static class ConnectionRunner implements Runnable{
        private Integer count;
        private AtomicInteger getCount;
        private AtomicInteger notGetCount;
        public ConnectionRunner(Integer count, AtomicInteger getCount, AtomicInteger notGetCount){
            this.count = count;
            this.getCount = getCount;
            this.notGetCount = notGetCount;
        }

        @Override
        public void run() {
            try {
                //保证同时进行
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (count > 0){
                try {
                    Connection connection = pool.fetchConnection(100);
                    if (connection != null) {
                        try {
                            connection.commit();
                        } finally {
                            getCount.incrementAndGet();
                            pool.releaseConnect(connection);
                        }
                    } else {
                        notGetCount.incrementAndGet();
                        System.out.println(Thread.currentThread().getName() + "   等待超时");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    count --;
                }
            }
            end.countDown();
        }
    }


}
