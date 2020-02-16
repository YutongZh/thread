package com.yt.base.container;

import java.util.concurrent.DelayQueue;

public class MyTest {

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Item<OrderData>> itemDelayed = new DelayQueue<>();
        new Thread(new PutQueue(itemDelayed)).start();
        new Thread(new FetchQueue(itemDelayed)).start();

        for (int i = 0; i < 15; i++) {
            Thread.sleep(500);
            System.out.println("时间过了" + i * 500);
        }
    }
}
