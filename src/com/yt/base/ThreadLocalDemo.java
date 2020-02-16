package com.yt.base;

import java.util.concurrent.TimeUnit;

/**
 * 需求：统计执行一个方法的耗时 也可以用aop实现，但好处是两个方法的调用可以在不同的类或方法中
 */
public class ThreadLocalDemo {

    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final Long end(){
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalDemo.begin();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("方法执行的时间是：" + ThreadLocalDemo.end() + "秒");
    }
}
