package com.yt.practice;

import java.util.concurrent.DelayQueue;

/**
 *  检查任务是否结束，放到缓存队列中，供一段时间查询，过期则从容器中移除。
 */
public class CheckTaskProcess{

    private static DelayQueue<ItemVO<String>> delayQueue = new DelayQueue();

    //单例模式
    private CheckTaskProcess(){}
    public static class CheckTaskHolder{
        public static CheckTaskProcess taskProcess = new CheckTaskProcess();
    }
    public static CheckTaskProcess getInstance(){
        return CheckTaskHolder.taskProcess;
    }

    /**
     * 从队列中获取 处理到期的任务
     */
    public static class FetchJobQueue implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    ItemVO<String> itemVO = delayQueue.take();
                    String jobName = itemVO.getData();
                    PendingJobPool.getJobMap().remove(jobName);
                    System.out.println( "job「"+ jobName +"」已从缓存删除");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 加入延时队列 任务完成后加入队列，到期从队列移除
     */
    public void PutJobQueue(String jobName, long expireTime){
        ItemVO<String> itemVO = new ItemVO<>(expireTime, jobName);
        delayQueue.offer(itemVO);
        System.out.println( "job「"+ jobName +"」已加入延时队列，还有"+ expireTime + "毫秒将从缓存移除");
    }

    /**
     * 开启守护线程，和框架同生共死
     */
    static{
        Thread thread = new Thread(new FetchJobQueue());
        thread.setDaemon(true);
        thread.start();
        System.out.println("****************开启任务过期检查的守护线程****************");
    }

}
