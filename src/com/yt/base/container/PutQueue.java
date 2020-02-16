package com.yt.base.container;

import java.math.BigDecimal;
import java.util.concurrent.DelayQueue;

/**
 * 将待支付订单加入队列
 */
public class PutQueue implements Runnable{

    private DelayQueue<Item<OrderData>> delayQueue;

    public PutQueue(DelayQueue<Item<OrderData>> delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        OrderData byOrder = new OrderData(122212232L, BigDecimal.valueOf(580));
        Item<OrderData> byItem = new Item<>(5000, byOrder);
        delayQueue.offer(byItem);
        System.out.println("5s后过期：" + byItem.toString());

        OrderData jdOrder = new OrderData(765556L, BigDecimal.valueOf(600));
        Item<OrderData> jdItem = new Item<>(8000, jdOrder);
        delayQueue.offer(jdItem);
        System.out.println("8s后过期：" + jdItem.toString());
    }
}
