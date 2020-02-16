package com.yt.base.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * java7提供的fork/join并发工具类（同步执行）  计算1+2+3+4+...+10;
 */
public class ForkJoin {

    static class CountTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD_NUM = 10000000;

        private Integer start;
        private Integer end;

        public CountTask(Integer start, Integer end){
            this.start = start;
            this.end = end;
        }
        @Override
        protected Integer compute() {
            Integer sum = 0;
            //如果任务小 就计算任务
            if (end - start < THRESHOLD_NUM){
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                //拆分任务
                int middle = (start + end) / 2;
                CountTask leftCount = new CountTask(start, middle);
                CountTask rightCount = new CountTask(middle + 1, end);

                //执行子任务
                leftCount.fork();
                rightCount.fork();

                //等待子任务执行完毕 合并结果
                Integer leftResult = leftCount.join();
                Integer rightResult = rightCount.join();
                sum = leftResult + rightResult;
            }
            return sum;
        }

        public static void main(String[] args) throws ExecutionException, InterruptedException {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            CountTask countTask = new CountTask(1, 100000000);
            //检查任务是否抛出异常或已经被取消
            boolean completedAbnormally = countTask.isCompletedAbnormally();
            System.out.println(completedAbnormally);
            long start = System.currentTimeMillis();
            ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(countTask);
            System.out.println("结果：" + forkJoinTask.get() + "；执行时间：" + (System.currentTimeMillis() - start) + "毫秒");

        }
    }
}
