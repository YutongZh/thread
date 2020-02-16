package com.yt.base;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 一个main方法 就有5个线程 java天生是多线程的
 */
public class MyThreadV1 {

    public static void main(String[] args) {
        //虚拟机线程管理的接口
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            String threadName = threadInfo.getThreadName();
            System.out.println(threadName);
        }
    }
}
