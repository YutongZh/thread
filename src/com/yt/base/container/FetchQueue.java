package com.yt.base.container;

import java.util.concurrent.DelayQueue;

/**
 * 从队列中获取
 */
public class FetchQueue implements Runnable{

    private DelayQueue<Item<OrderData>> delayQueue;

    public FetchQueue(DelayQueue<Item<OrderData>> delayQueue) {
        this.delayQueue = delayQueue;
    }
    @Override
    public void run() {
        while(true){
            try {
                Item<OrderData> take = delayQueue.take();
                OrderData data = take.getData();
                System.out.println("收到过期订单信息" + data.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
