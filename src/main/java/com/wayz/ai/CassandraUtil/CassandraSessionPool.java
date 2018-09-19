package com.wayz.ai.CassandraUtil;

import com.datastax.driver.core.Session;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class CassandraSessionPool extends GenericObjectPool<Session> {
	private static final Logger logger = LoggerFactory.getLogger(CassandraSessionPool.class);
	
	private static CassandraSessionFactory factory;
	private static CassandraSessionPool sessionPool;

	public static CassandraSessionPool getInstance() {
		return sessionPool;
	}
	
	private CassandraSessionPool(PooledObjectFactory<Session> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}
	
    public static void initSessionPool() {
    	CassandraConfig config = new CassandraConfig();
		String   contactPointsStr = CassandraConfigurer.getString(Cassandra.CONTACT_POINTS);
		Integer  port             = CassandraConfigurer.getInteger(Cassandra.PORT);
		Boolean  auth             = CassandraConfigurer.getBoolean(Cassandra.AUTH);
		String   username         = CassandraConfigurer.getString(Cassandra.USERNAME);
		String   password         = CassandraConfigurer.getString(Cassandra.PASSWORD);
		String   keyspace         = CassandraConfigurer.getString(Cassandra.KEYSPACE);
		
    	int  maxTotal      = CassandraConfigurer.getInteger(Cassandra.SESSION_POOL_MAX_TOTAL, config.getPoolMaxTotal());
    	int  maxIdle       = CassandraConfigurer.getInteger(Cassandra.SESSION_POOL_MAX_IDLE, config.getPoolMaxIdle());
    	int  minIdle       = CassandraConfigurer.getInteger(Cassandra.SESSION_POOL_MIN_IDLE, config.getPoolMinIdle());
    	long maxWaitMillis = CassandraConfigurer.getLong(Cassandra.SESSION_POOL_MAX_WAIT_MILLIS, config.getPoolMaxWaitMillis());
    	
    	config.setContactPoints(contactPointsStr);
    	config.setPort(port);
    	config.setAuth(auth);
    	config.setUsername(username);
    	config.setPassword(password);
    	config.setKeyspace(keyspace);
    	
    	config.setPoolMaxTotal(maxTotal);
    	config.setPoolMaxIdle(maxIdle);
    	config.setPoolMinIdle(minIdle);
    	config.setPoolMaxWaitMillis(maxWaitMillis);
    	
    	initSessionPool(config);
    }
    
	public static void initSessionPool(CassandraConfig config) {
		String   contactPointsStr = config.getContactPoints();
		String[] contactPoints    = contactPointsStr.split(",");
		
    	factory = new CassandraSessionFactory(contactPoints, config.getPort(), config.getAuth(), config.getUsername(), config.getPassword(), config.getKeyspace());
    	
    	GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    	poolConfig.setMaxTotal(config.getPoolMaxTotal()); // 池中的最大连接数
    	poolConfig.setMaxIdle(config.getPoolMaxIdle());   // 最大的空闲连接数
    	poolConfig.setMinIdle(config.getPoolMinIdle());   // 最小的空闲连接数
        poolConfig.setMaxWaitMillis(config.getPoolMaxWaitMillis()); // 当连接池资源耗尽时，调用者最大阻塞的时间，超时时抛出异常
        
        sessionPool = new CassandraSessionPool(factory, poolConfig);
	}
    
	public Session borrowSession() throws Exception {
		logger.info("active: " + sessionPool.getNumActive() + ", idle: " + sessionPool.getNumIdle() + ", waiters: " + sessionPool.getNumWaiters());
		return borrowObject();
	}
	
	public void returnSession(Session session) {
		returnObject(session);
	}

	@Override
	public void close() {
		super.close();
		factory.close();
	}
}
