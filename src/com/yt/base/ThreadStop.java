package com.yt.base;

/**
 *  怎样让java线程停止运行
 */
public class ThreadStop {

    public static class ThreadStopV1 extends Thread{
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while (!isInterrupted()){
                System.out.println(name);
            }
            System.out.println(name + "interupt flag is " + isInterrupted());
        }
    }

    public static class RunableStop implements Runnable{
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while(true) {
                System.out.println(name + "一路顺风   " + System.currentTimeMillis());
            }
        }
    }

    public static class RunableStopV2 implements Runnable{
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while(Thread.currentThread().isInterrupted()) {
                System.out.println(name + "一路顺风   " + System.currentTimeMillis());
            }
        }
    }

    public static class RunableStopV3 implements Runnable{
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("isInterrupt flag is========== " + Thread.currentThread().isInterrupted());
                    e.printStackTrace();
                    Thread.currentThread().interrupt();  //需手动调用才能将标示位置为true
                }
                System.out.println(name + "一路顺风   " + System.currentTimeMillis());
            }
            System.out.println(name + " isInterrupt flag is   " + Thread.currentThread().isInterrupted());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //jdk 不建议使用 stop()；resume()；suspend();
        RunableStop runableStop = new RunableStop();
        Thread thread = new Thread(runableStop);
        thread.start();
        Thread.sleep(200);
        thread.stop();  //jdk 不建议使用


        //Thread 中断操作  主线程给T线程打个招呼 全靠子线程自己判断标示位自己处理
        ThreadStopV1 threadStopV1 = new ThreadStopV1();
        threadStopV1.start();
        Thread.sleep(2000);
        threadStopV1.interrupt();

        //runable 中断操作 静态方法
        RunableStopV2 runableStopV2 = new RunableStopV2();
        Thread threadV2 = new Thread(runableStopV2);
        threadV2.start();
        Thread.sleep(200);
        threadV2.interrupt();

        //子线程有抛出interception异常 需手动interrupt（）
        RunableStopV3 runableStopV3 = new RunableStopV3();
        Thread threadV3 = new Thread(runableStopV3);
        threadV3.start();
        Thread.sleep(2000);
        threadV3.interrupt();
    }
}
