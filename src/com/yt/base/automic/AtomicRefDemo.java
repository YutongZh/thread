package com.yt.base.automic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicRefDemo {

    static AtomicReference<UserInfo> atomicReference = new AtomicReference<>();

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("zyt", 18);
        atomicReference.set(userInfo);

        UserInfo userInfoBob = new UserInfo("Bob", 19);
        boolean b = atomicReference.compareAndSet(userInfo, userInfoBob);

        System.out.println("是否替换成功：" + b);
        System.out.println(atomicReference);
        System.out.println(atomicReference.get().getName());
        System.out.println(userInfo);
    }


    static class UserInfo {
        private String name;
        private int age;

        public UserInfo(String name, int age){
            this.age = age;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
