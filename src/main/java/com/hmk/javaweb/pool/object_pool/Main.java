package com.hmk.javaweb.pool.object_pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


/**
 *
 * https://blog.csdn.net/qq447995687/article/details/80233621
 *
 *   要实现一个连接池首先需要3个基本的类，
 *   PooledObjec池中对象，PooledObjectFactory对象工厂，ObjectPool对象池。
 *   由于ObjectPool缓存的是一个对象的包装类型即PooledObjec，所以在PooledObjectFactory获得对象的时候需将实际对象进行包装。
 */
public class Main {
    public static int COUNT = 10;
    
    public static void main(String[] args) throws Exception {
        ConnectionFactory orderFactory = new ConnectionFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(5);
        //设置获取连接超时时间
        config.setMaxWaitMillis(1000);
        ConnectionPool connectionPool = new ConnectionPool(orderFactory, config);
        for (int i = 0; i < COUNT; i++) {
            Connection o = connectionPool.borrowObject();
            System.out.println("brrow a connection: " + o +" active connection:"+connectionPool.getNumActive());
            //释放(归还)连接
            connectionPool.returnObject(o);
        }
    }
}
