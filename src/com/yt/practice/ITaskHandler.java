package com.yt.practice;

public interface ITaskHandler<T, R> {

    TaskResult<R> executeTask(T taskData) throws InterruptedException;
}
