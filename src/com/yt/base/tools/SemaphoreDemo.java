package com.yt.base.tools;

import com.yt.base.pool.ConnectionDriver;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量  控流神器
 */
public class SemaphoreDemo {

    private static LinkedList<Connection> POOL = new LinkedList<>();

    //表示已用的数据库连接  10个许可证
    Semaphore userLessSema;
    //表示可用的数据库连接
    Semaphore usefulSema;

    static {
        for (int i = 0; i < 10; i++) {
            POOL.addLast(new MyConnection());
        }
    }

    //通过构造方法创建连接
    public SemaphoreDemo(){
        this.userLessSema = new Semaphore(0);
        this.usefulSema = new Semaphore(10);
    }
    /**
     * 获取连接
     */
    public Connection fetchConnection() throws InterruptedException {
            //获取可用的许可证
            usefulSema.acquire();
            Connection connection;
            synchronized (POOL) {
                connection = POOL.removeFirst();
            }
            //归还已用的许可证
            userLessSema.release();
            return connection;

    }

    /**
     * 释放连接
     */
    public void releaseConnection(Connection connection) throws InterruptedException {
        if (connection != null) {
            //获得已用许可证
            System.out.println("当前有 " + usefulSema.getQueueLength() + "可用的链接");
            userLessSema.acquire();
            synchronized (POOL){
                POOL.addLast(connection);
            }
            //归还可用的许可证书
            usefulSema.release();
        }
    }

    static class PoolTest implements Runnable{
        private SemaphoreDemo semaphoreDemo;
        public PoolTest(SemaphoreDemo semaphoreDemo){
            this.semaphoreDemo = semaphoreDemo;
        }
        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();
                Connection connection = semaphoreDemo.fetchConnection();
                System.out.println(Thread.currentThread().getName() + " 获取链接...  获取链接时间" + (System.currentTimeMillis() - start) + "毫秒");
                TimeUnit.MILLISECONDS.sleep(1000);
                semaphoreDemo.releaseConnection(connection);
                System.out.println(Thread.currentThread().getName() + " 释放链接 ... ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        for (int i = 0; i < 20; i++) {
            new Thread(new PoolTest(semaphoreDemo)).start();
        }
    }
}
