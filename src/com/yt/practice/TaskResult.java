package com.yt.practice;

import com.sun.deploy.util.StringUtils;

/**
 *  返回任务结果
 */
public class TaskResult<R> {
    private final ResultTypeEnum resultType;
    private final R data;
    private final String errorMsg;

    public TaskResult(ResultTypeEnum resultType, R data, String errorMsg) {
        this.resultType = resultType;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public ResultTypeEnum getResultType() {
        return resultType;
    }

    public R getData() {
        return data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "resultType=" + resultType +
                ", data=" + data +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
