package com.yt.base.automic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArrayDemo {

    static int[] arr = new int[]{1,2};
    static AtomicIntegerArray air = new AtomicIntegerArray(arr);

    public static void main(String[] args) {
        air.getAndSet(0, 100);
        System.out.println(air);
        System.out.println(arr[0]);
    }
}
