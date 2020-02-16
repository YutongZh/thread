package com.yt.base.lock;

public interface GoodsService {
    void setNum(int num) throws InterruptedException;

    GoodsInfo getNum() throws InterruptedException;

}
