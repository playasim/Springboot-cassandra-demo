package com.wayz.ai.CassandraUtil;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.*;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class CassandraSessionFactory extends BasePooledObjectFactory<Session> {
	private Cluster cluster;
	private String  keyspace;
	
	public CassandraSessionFactory(String[] contactPoints, int port, boolean auth, String username, String password, String keyspace) {
		this.keyspace = keyspace;
		
		// 认证
		AuthProvider authProvider = AuthProvider.NONE;
		if (auth) {
			authProvider = new PlainTextAuthProvider(username, password);
		}
		// 负载
		DCAwareRoundRobinPolicy policy = DCAwareRoundRobinPolicy.builder().build();
        LoadBalancingPolicy     lbp    = new TokenAwarePolicy(policy);
        // 读超时、连接超时
        SocketOptions socketOptions = new SocketOptions().setReadTimeoutMillis(30 * 1000).setConnectTimeoutMillis(30 * 1000);
        // 连接池(集群在同一个机房用HostDistance.LOCAL, 不同的机房用HostDistance.REMOTE, 忽略用HostDistance.IGNORED)
        PoolingOptions poolingOptions= new PoolingOptions()
                .setMaxRequestsPerConnection(HostDistance.LOCAL, 64) //每个连接最多允许64个并发请求
                .setCoreConnectionsPerHost(HostDistance.LOCAL, 2)    //和集群里的每个机器都至少有2个连接
                .setMaxConnectionsPerHost(HostDistance.LOCAL, 6);    //和集群里的每个机器都最多有6个连接
        // 查询(设置一致性级别)
        QueryOptions queryOptions = new QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE);
        // 重试策略
        RetryPolicy retryPolicy = DefaultRetryPolicy.INSTANCE;
        // 重连策略
        ReconnectionPolicy reconnectionPolicy = new ConstantReconnectionPolicy(1000);
		
        this.cluster = Cluster.builder()
                .addContactPoints(contactPoints)
                .withAuthProvider(authProvider)
                .withLoadBalancingPolicy(lbp)
                .withSocketOptions(socketOptions)
                .withPoolingOptions(poolingOptions)
                .withQueryOptions(queryOptions)
                .withRetryPolicy(retryPolicy)
                .withReconnectionPolicy(reconnectionPolicy)
                .withPort(port)
                .build();
	}

	@Override
	public Session create() throws Exception {
		if (keyspace == null) {
			return cluster.connect();
		}
		return cluster.connect(keyspace);
	}
	
	@Override
	public PooledObject<Session> wrap(Session obj) {
		return new DefaultPooledObject<Session>(obj); 
	}

	@Override
	public void destroyObject(PooledObject<Session> obj) throws Exception {
		Session session = obj.getObject();
		session.close();
	}

	public void close() {
		cluster.close();
	}
	
	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
}
