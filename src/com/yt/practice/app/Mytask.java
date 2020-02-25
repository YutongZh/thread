package com.yt.practice.app;

import com.yt.practice.ITaskHandler;
import com.yt.practice.ResultTypeEnum;
import com.yt.practice.TaskResult;

import java.util.Random;

public class Mytask implements ITaskHandler<Integer, Integer> {

    @Override
    public TaskResult<Integer> executeTask(Integer taskData){

        Random random = new Random();
        int flag = random.nextInt(500);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return new TaskResult<>(ResultTypeEnum.EXCEPTION, -1, e.getMessage());
        }
        if (flag <=300){
            return new TaskResult<>(ResultTypeEnum.SUCCESS,taskData + flag, "");
        } else if (flag >=300 && flag <=400){
            return new TaskResult<>(ResultTypeEnum.FAIL, -1,"Failure");
        } else {
            try {
                throw new RuntimeException("异常了！");
            } catch (Exception e){
                return new TaskResult<>(ResultTypeEnum.EXCEPTION, -1, e.getMessage());
            }
        }
    }
}
