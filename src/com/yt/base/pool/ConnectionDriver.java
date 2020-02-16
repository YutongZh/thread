package com.yt.base.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

//创建connection
public class ConnectionDriver implements InvocationHandler {
    /**
     * proxy：就是代理对象，newProxyInstance方法的返回对象
     * method：调用的方法
     * args: 方法中的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("commit".equals(method.getName())){
            TimeUnit.MILLISECONDS.sleep(100L);
        }
        return null;
    }

    /**
     * loader: 用哪个类加载器去加载代理对象
     * interfaces:动态代理类需要实现的接口
     * h:动态代理方法在执行时，会调用h里面的invoke方法去执行
     */

    public static final Connection createConntion(){
        return  (Connection) Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(),
                    new Class[]{Connection.class}, new ConnectionDriver());
    }


}
