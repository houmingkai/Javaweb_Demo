package com.hmk.javaweb.pool.object_pool;

import com.hmk.javaweb.pool.object_pool.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionFactory  extends BasePooledObjectFactory<Connection> {

    private AtomicInteger idCount = new AtomicInteger(1);

    @Override
    public Connection create() throws Exception {
        return new Connection(idCount.getAndAdd(1));
    }

    @Override
    public PooledObject<Connection> wrap(Connection conn) {
        //包装实际对象
        return new DefaultPooledObject<>(conn);
    }
}
