package com.yt.practice;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ItemVO<T> implements Delayed {

    //超时的时刻
    private long activeTime;
    private T data;

    //activeTime是个过期时长
    public ItemVO(long activeTime, T data) {
        this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();
        this.data = data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public T getData() {
        return data;
    }

    //获取剩余时间
    @Override
    public long getDelay(TimeUnit unit) {
        long remainTime = unit.convert(this.activeTime - System.nanoTime(),
                TimeUnit.NANOSECONDS);
        return remainTime;
    }

    @Override
    public int compareTo(Delayed delayed) {
        long l = this.getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS);
        return l == 0 ? 0 : (l > 0 ? 1 : -1);
    }

    @Override
    public String toString() {
        return "Item{" +
                "activeTime=" + activeTime +
                ", data=" + data +
                '}';
    }
}
