package com.yt.base.pool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 手写连接池
 */
public class ConnectionPool {

    private static final LinkedList<Connection> POOL = new LinkedList<>();

    //通过构造方法创建连接
    public ConnectionPool(int poolNum){
        for (int i = 0; i < poolNum; i++) {
            POOL.add(ConnectionDriver.createConntion());
        }
    }

    //归还一个连接
    public void releaseConnect(Connection connection){
        if (connection != null){
            synchronized (POOL){
                POOL.addLast(connection);
                //连接释放后通知
                POOL.notifyAll();
            }
        }
    }

    //获取一个连接  超过超时时间则返回null
    public Connection fetchConnection(long millisecond) throws InterruptedException {
        synchronized (POOL){
            //完全超时 有则直接返回 否则加入等待队列
            if(millisecond <= 0){
                while (POOL.isEmpty()){
                    POOL.wait();
                }
                return POOL.removeFirst();
            } else {
                long future = System.currentTimeMillis() + millisecond;
                long remainTime = millisecond;
                while (POOL.isEmpty() && remainTime > 0){
                    POOL.wait(remainTime);
                    remainTime = future - System.currentTimeMillis();
                }
                Connection connection = null;
                if (!POOL.isEmpty()){
                    connection = POOL.removeFirst();
                }
                return connection;
            }
        }
    }
}
