package com.yt.base.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * condition
 * 需求：公里数 和 配送地址发生变化通知push
 *
 * 一个锁可以对应多个condition ，尽量使用signal，而不用signalALL
 */
public class ConditionDemo {
    //公里数未达标
    static boolean kmNotPass = Boolean.TRUE;
    //地址未发生变化
    static boolean addNotChange = Boolean.TRUE;

    static Lock lock = new ReentrantLock();
    static Condition kmCondition = lock.newCondition();
    static Condition siteCondition = lock.newCondition();

    static class WaitKm implements Runnable{
        @Override
        public void run() {
            lock.lock();
            try{
                while (kmNotPass){
                    try {
                        System.out.println("等待收到公里消息中..");
                        kmCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("推送消息，你的公里数");
            } finally {
                lock.unlock();
            }
        }
    }

    static class WaitAdd implements Runnable{
        @Override
        public void run() {
            lock.lock();
            try{
                while (addNotChange){
                    try {
                        System.out.println("等待收到地址消息中..");
                        siteCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("推送消息，快递到达的地址");
            }finally {
                lock.unlock();
            }
        }
    }

    static class NotifyKm implements Runnable{
        @Override
        public void run() {
            lock.lock();
            try {
                kmNotPass = false;
                kmCondition.signal();
                System.out.println("公里数发生改变了，通知PUSH...");
            } finally {
                lock.unlock();
            }
        }
    }

    static class NotifyAdd implements Runnable{
        @Override
        public void run() {
            lock.lock();
            try{
                addNotChange = false;
                siteCondition.signal();
                System.out.println("配送地址发生改变了，通知PUSH....");
            } finally {
                lock.unlock();
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
