package com.yt.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工作类
 */
public class JobInfoDTO<R> {

    //工作的唯一表示，不同的业务生成规则不一致，所以不用id
    private final String jobName;

    //提交此工作的任务数
    private final Integer taskNum;

    //当前任务的处理器
    private ITaskHandler taskHandler;

    //处理成功任务数
    private AtomicInteger taskSuccessNum;

    //已完成任务数
    private AtomicInteger taskFinishNum;

    //工作中任务结果详情队列，拿结果从头拿，放结果从尾部放
    LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;

    //工作完成保存的时间，超过这个时间从缓存中清除过期时间，由调用者定义
    private final Long expireTime;

    public JobInfoDTO(String jobName, ITaskHandler taskHandler, Integer taskNum, Long expireTime) {
        this.jobName = jobName;
        this.taskNum = taskNum;
        this.taskHandler = taskHandler;
        this.taskSuccessNum = new AtomicInteger(0);
        this.taskFinishNum = new AtomicInteger(0);;
        this.taskDetailQueue = new LinkedBlockingDeque<>(taskNum);
        this.expireTime = expireTime;
    }

    public int getHandSuccessNum() {
        return taskSuccessNum.get();
    }

    public int getTaskFinishNum() {
        return taskFinishNum.get();
    }

    public int getTaskFailNum() {
        return taskFinishNum.get() - taskSuccessNum.get();
    }

    public String getCurProcess(){
        return "【Sucess" + getHandSuccessNum() + "条】/" + "【Finish" + getTaskFinishNum() +"条】" +
                " 【Total" + taskNum + "】";
    }

    //获取任务结果详情 暴露给调用者最简单的list
    public List<TaskResult<R>> getTaskDetail() {
        List resultList = new LinkedList();
        TaskResult<R> taskResult;
        //从阻塞队列中反复拿，一直拿完为止
        while ((taskResult = taskDetailQueue.pollFirst()) != null){
            resultList.add(taskResult);
        }
        return resultList;
    }

    /**
     *  添加到任务结果队列 并完成计数
     *  可能导致任务数量和队列数的不一致，是否考虑加锁？从业务角度来讲，保证最终一致性即可
     */
    public void addTaskDetail(TaskResult<R> result, CheckTaskProcess checkTaskProcess){
        if (result != null && ResultTypeEnum.SUCCESS.equals(result.getResultType())){
            taskSuccessNum.incrementAndGet();
        }
        taskFinishNum.incrementAndGet();
        taskDetailQueue.addFirst(result);
        if (getTaskFinishNum() == taskNum){
            checkTaskProcess.PutJobQueue(jobName, expireTime);
        }
    }

    public ITaskHandler getTaskHandler() {
        return taskHandler;
    }
}
