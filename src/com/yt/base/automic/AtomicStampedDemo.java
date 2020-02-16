package com.yt.base.automic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 带版本号的原子操作类  解决CAS 的 ABA问题
 */
public class AtomicStampedDemo {

    static AtomicStampedReference<String> asr = new AtomicStampedReference("zyt", 0);

    public static void main(String[] args) throws InterruptedException {
        String oldValue = asr.getReference();
        int oldStamp = asr.getStamp();

        Thread rightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = asr.compareAndSet(oldValue, oldValue + "_new", oldStamp, oldStamp + 1);
                System.out.println(Thread.currentThread().getName() + ";旧值为:" + oldValue
                +";旧版本号为：" + oldStamp + ";新值为" + asr.getReference() + ";新的版本号为：" + asr.getStamp()
            + "是否替换成功：" + b);
            }
        });


        Thread errorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String oldReference = asr.getReference();
                boolean set = asr.compareAndSet(oldReference, oldReference+ "_java", oldStamp, oldStamp + 1);
                System.out.println(Thread.currentThread().getName() + ";旧值为:" + oldReference
                        +";旧版本号为：" + oldStamp + ";新值为" + asr.getReference() + ";新的版本号为：" + asr.getStamp()
                        + "是否替换成功：" + set);
            }
        });

        rightThread.start();
        rightThread.join();
        errorThread.start();
        errorThread.join();
        System.out.println(asr.getReference() + ":::" + asr.getStamp());

    }
}
