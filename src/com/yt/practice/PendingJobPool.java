package com.yt.practice;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 并发框架
 */
public class PendingJobPool<T, R> {

    //缓存任务队列
    private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue(5000);

    //线程数大小  保守估计CPU核心数
    private static final int THREAD_COUNTS = Runtime.getRuntime().availableProcessors() * 4;

    //自定义线程池 （固定大小 ，有界）
    private static ExecutorService threadPoolExecutor = new ThreadPoolExecutor(THREAD_COUNTS,
            THREAD_COUNTS, 60, TimeUnit.SECONDS, taskQueue);

    //缓存JOB的容器
    private static ConcurrentHashMap<String, JobInfoDTO> jobInfoMap = new ConcurrentHashMap();

    //检查任务类
    private static CheckTaskProcess checkTaskProcess = CheckTaskProcess.getInstance();

    //单例模式获取
    private PendingJobPool(){}
    private static class JobPoolHolder {
        public static PendingJobPool pool = new PendingJobPool();
    }
    public static PendingJobPool getInstance(){
        return JobPoolHolder.pool;
    }

    public static Map<String, JobInfoDTO> getJobMap(){
        return jobInfoMap;
    }

    //注册JOB到容器
    public void registJob(String jobName, ITaskHandler taskHandler, Integer taskNum, Long expireTime){
        JobInfoDTO<R> jobInfoDTO = new JobInfoDTO(jobName, taskHandler, taskNum, expireTime);
        //不能直接put，若存在job，提示已注册
        if (jobInfoMap.putIfAbsent(jobName, jobInfoDTO) != null){
            throw new RuntimeException(jobName + "already regist！");
        }
    }

    //获取jobInfo
    private JobInfoDTO<R> getJobInfo(String jobName){

        JobInfoDTO<R> jobInfoDTO = jobInfoMap.get(jobName);
        if (jobInfoDTO == null){
            throw new RuntimeException(jobName + "is illeagl");
        }
        return jobInfoDTO;
    }

    //提交任务到线程池
    public void submitTask (String jobName, T taskData){
        JobInfoDTO<R> jobInfo = getJobInfo(jobName);
        PendingTask<T, R> pendingTask = new PendingTask(jobInfo, taskData);
        threadPoolExecutor.execute(pendingTask);
    }


    //将任务包装成PendingTask提交给线程池
    public class PendingTask<T, R> implements Runnable{

        private JobInfoDTO<R> jobInfoDTO;
        private T taskData;

        public PendingTask(JobInfoDTO<R> jobInfoDTO, T taskData) {
            this.jobInfoDTO = jobInfoDTO;
            this.taskData = taskData;
        }

        @Override
        public void run() {
            TaskResult taskResult = null;
            try {
                ITaskHandler taskHandler = jobInfoDTO.getTaskHandler();
                taskResult = taskHandler.executeTask(taskData);
                if (taskResult == null) {
                    taskResult = new TaskResult(ResultTypeEnum.FAIL, taskResult, "taskResult is null");
                } else {
                    if (taskResult.getResultType() == null) {
                        if (taskResult.getErrorMsg() != null) {
                            taskResult = new TaskResult(ResultTypeEnum.EXCEPTION, taskResult,
                                    "resultType is return null, because " + taskResult.getErrorMsg());
                        } else {
                            taskResult = new TaskResult(ResultTypeEnum.EXCEPTION, taskResult,
                                    "resultType and errorMsg is return null");
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                taskResult = new TaskResult(ResultTypeEnum.EXCEPTION, taskResult, e.getMessage());
            } finally {
                jobInfoDTO.addTaskDetail(taskResult, checkTaskProcess);
            }
        }
    }

    // 获取任务详情
    public <R> List<TaskResult<R>> getTaskDetail(String jobName){
        JobInfoDTO<R> jobInfo = (JobInfoDTO<R>) getJobInfo(jobName);
        return jobInfo.getTaskDetail();
    }

    // 获取任务进度
    public String getTaskProcess(String jobName){
        JobInfoDTO<R> jobInfo = getJobInfo(jobName);
        return jobInfo.getCurProcess();
    }

    public int getSumTask(String jobName){
        JobInfoDTO<R> jobInfo = getJobInfo(jobName);
        return jobInfo.getTaskFinishNum();
    }
}
