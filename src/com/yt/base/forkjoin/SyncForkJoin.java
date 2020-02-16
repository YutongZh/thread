package com.yt.base.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 同步 fork/join  需求：遍历文件夹目录下指定类型的文件 （无返回值）
 */
public class SyncForkJoin extends RecursiveAction {

    private File file;
    public SyncForkJoin(File file){
        this.file = file;
    }
    @Override
    protected void compute() {
        File[] files = file.listFiles();
        List<SyncForkJoin> syncForkJoinList = new ArrayList<>();
        for (File curFile : files) {
            if (curFile.isDirectory()){
                syncForkJoinList.add(new SyncForkJoin(curFile));
            } else {
                if (curFile.getAbsoluteFile().getName().endsWith("txt")){
                    System.out.println("找到的文件为：  " + curFile.getAbsoluteFile().getName());
                }
            }
            Collection<SyncForkJoin> syncForkJoins = invokeAll(syncForkJoinList);
            if (!syncForkJoins.isEmpty()){
                for (SyncForkJoin syncForkJoin : syncForkJoins) {
                    syncForkJoin.join();
                }
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        File file = new File("/Users/yutong/Downloads");
        SyncForkJoin syncForkJoin = new SyncForkJoin(file);
        forkJoinPool.execute(syncForkJoin);  //异步执行

        System.out.println("main is running ...");

        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        System.out.println(sum);
        syncForkJoin.join();  //阻塞方法 有可能forkJoinPool方法还没执行完就结束，所以阻塞
        System.out.println("The end ...");
    }
}
