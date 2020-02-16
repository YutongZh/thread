package com.yt.base;

import java.util.concurrent.TimeUnit;

/**
 * notify 和 notifyAll
 * 需求：公里数 和 配送地址发生变化通知push
 */
public class NotifyAllDemo {
    //公里数未达标
    static boolean kmNotPass = Boolean.TRUE;
    //地址未发生变化
    static boolean addNotChange = Boolean.TRUE;

    static Object lock = new Object();

    static class WaitKm implements Runnable{
        @Override
        public void run() {
            synchronized (lock){
                while (kmNotPass){
                    try {
                        System.out.println("等待收到公里消息中..");
                        lock.wait();  //wait() 会释放锁 进入wait队列  object方法 注意用当前对象调用 锁的是对象 不是线程
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("推送消息，你的公里数");
            }
        }
    }

    static class WaitAdd implements Runnable{
        @Override
        public void run() {
            synchronized (lock){
                while (addNotChange){
                    try {
                        System.out.println("等待收到地址消息中..");
                        lock.wait();  //wait() 会释放锁 进入wait队列
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("推送消息，快递到达的地址");
            }
        }
    }

    static class NotifyKm implements Runnable{
        @Override
        public void run() {
            synchronized (lock){
                kmNotPass = false;
                //lock.notify(); 随机唤醒一个，会导致错误的启用
                lock.notifyAll(); //唤醒所有wait的线程，建议使用 notify/notify不会立即释放锁
                System.out.println("公里数发生改变了，通知PUSH...");
            }
        }
    }

    static class NotifyAdd implements Runnable{
        @Override
        public void run() {
            synchronized (lock){
                addNotChange = false;
                lock.notify();
                System.out.println("配送地址发生改变了，通知PUSH....");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new WaitAdd()).start();
        new Thread(new WaitKm()).start();
        TimeUnit.SECONDS.sleep(1L);
        //new Thread(new NotifyAdd()).start();
        new Thread(new NotifyKm()).start();
    }
}
