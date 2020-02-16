package com.yt.base;

/**
 * 守护线程
 */
public class DaemonThread extends Thread{

    @Override
    public void run() {
        try{
            while (!isInterrupted()){
                System.out.println(Thread.currentThread().getName() + "===" + System.currentTimeMillis());
            }
        }finally {
            System.out.println("finally 执行了.....");
        }
    }

    /**
     * 主线程运行完毕  子线程生命周期也结束了
     *  注意守护线程的finally的不一定会执行。。
     */
    public static void main(String[] args) throws InterruptedException {
        DaemonThread daemonThread = new DaemonThread();
        //守护线程一定要放在start方法之前
        daemonThread.setDaemon(true);
        daemonThread.start();
        Thread.sleep(5);
        //daemonThread.interrupt();
    }
}
