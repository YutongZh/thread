package com.yt.base.lock;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyReadWriteLock implements GoodsService{
    private GoodsInfo goodsInfo;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public MyReadWriteLock(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public void setNum(int num) throws InterruptedException {
        writeLock.lock();
        try {
            TimeUnit.MILLISECONDS.sleep(5);
            this.goodsInfo.setNum(num);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public GoodsInfo getNum() throws InterruptedException {
        readLock.lock();
        try {
            TimeUnit.MILLISECONDS.sleep(5);
            return this.goodsInfo;
        } finally {
            readLock.unlock();
        }
    }
}