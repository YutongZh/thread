package com.yt.base.lock;

import java.util.concurrent.TimeUnit;

public class SynLock implements GoodsService{

    private GoodsInfo goodsInfo;

    public SynLock(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public synchronized void setNum(int num) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5);
        this.goodsInfo.setNum(num);
    }

    @Override
    public synchronized GoodsInfo getNum() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(5);
        return this.goodsInfo;
    }
}
