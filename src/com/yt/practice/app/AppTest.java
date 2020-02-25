package com.yt.practice.app;

import com.yt.practice.PendingJobPool;
import com.yt.practice.TaskResult;

import java.util.List;
import java.util.Random;

public class AppTest {

    public static final String JOB_NAME = "CREATE_PDF";
    public static final int TASK_NUM = 1000;
    public static final Long EXPIRE_TIME = 5*1000L;


    /**
     * 用户查询方法
     */
    public static class QueryResult implements Runnable{

        private PendingJobPool pendingJobPool;

        public QueryResult(PendingJobPool pendingJobPool) {
            this.pendingJobPool = pendingJobPool;
        }

        @Override
        public void run() {
            int i = 0;
            while (i < 200){
                List<TaskResult> taskDetail = pendingJobPool.getTaskDetail(JOB_NAME);
                if (!taskDetail.isEmpty()){
                    System.out.println("当前执行进度为：：" + pendingJobPool.getTaskProcess(JOB_NAME));
                    System.out.println("当前执行的内容详情：：" + taskDetail.toString());
                }

                if (pendingJobPool.getSumTask(JOB_NAME) == TASK_NUM){
                    break;
                }
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }

    }
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Mytask mytask = new Mytask();
        PendingJobPool pendingJobPool = PendingJobPool.getInstance();
        pendingJobPool.registJob(JOB_NAME, mytask, TASK_NUM, EXPIRE_TIME);
        Random random = new Random();
        for (int i = 0; i < TASK_NUM; i++) {
            pendingJobPool.submitTask(JOB_NAME, random.nextInt(1000));
        }
        Thread t = new Thread(new QueryResult(pendingJobPool));
        t.start();
        t.join();
        System.out.println(System.currentTimeMillis() - start);
    }
}
