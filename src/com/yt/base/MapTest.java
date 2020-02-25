package com.yt.base;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

    public static class MyMap extends HashMap{

    }
    public static void main(String[] args) {
        MyMap hashMap = new MyMap();
        hashMap.put("1", "2");

        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
