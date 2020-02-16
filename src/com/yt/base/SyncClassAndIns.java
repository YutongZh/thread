package com.yt.base;

import java.util.concurrent.TimeUnit;

public class SyncClassAndIns {

    public static class SyncClass extends Thread{
        @Override
        public void run() {
            System.out.println("类锁开始。ThreadName=" + Thread.currentThread().getName());
            syncClass();
        }
    }

    public static class SyncIntOne implements Runnable{
        private SyncClassAndIns syncClassAndIns;
        public SyncIntOne(SyncClassAndIns syncClassAndIns){
            this.syncClassAndIns = syncClassAndIns;
        }
        @Override
        public void run() {
            System.out.println("对象锁开始...SyncIntOne ThreadName=" + Thread.currentThread().getName());
            syncClassAndIns.syncIntOne();
        }
    }

    public static class SyncIntTwo implements Runnable{
        private SyncClassAndIns syncClassAndIns;
        public SyncIntTwo(SyncClassAndIns syncClassAndIns){
            this.syncClassAndIns = syncClassAndIns;
        }
        @Override
        public void run() {
            System.out.println("对象锁开始...SyncIntTwo ThreadName=" + Thread.currentThread().getName());
            syncClassAndIns.syncIntTwo();
        }
    }

    public synchronized static void syncClass(){
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("syncClass is GO...");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("syncClass is END...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void syncIntOne(){
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("syncIntOne is GO..." + this.toString());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("syncIntOne is END..." + this.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void syncIntTwo(){
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("syncIntTwo is GO..." + this.toString());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("syncIntTwo is END..." + this.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SyncClassAndIns syncClassAndIns = new SyncClassAndIns();

        new Thread(new SyncIntOne(syncClassAndIns)).start();
        new Thread(new SyncIntTwo(syncClassAndIns)).start();

        new SyncClass().start();
        new SyncClass().start();
    }
}
