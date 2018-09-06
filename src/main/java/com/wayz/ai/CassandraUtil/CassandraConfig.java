package com.wayz.ai.CassandraUtil;

public class CassandraConfig {
	private String  contactPoints;
	private Integer port;
	private Boolean auth;
	private String  username;
	private String  password;
	private String  keyspace;
	
	private int  poolMaxTotal      = 10;
	private int  poolMaxIdle       = 10;
	private int  poolMinIdle       = 0;
	private long poolMaxWaitMillis = 30 * 1000l;
	
	public CassandraConfig() {}

	public CassandraConfig(String contactPoints, Integer port) {
		this.contactPoints = contactPoints;
		this.port          = port;
		this.auth          = false;
	}

	public CassandraConfig(String contactPoints, Integer port, String keyspace) {
		this.contactPoints = contactPoints;
		this.port          = port;
		this.auth          = false;
		this.keyspace      = keyspace;
	}

	public CassandraConfig(String contactPoints, Integer port, String username, String password) {
		this.contactPoints = contactPoints;
		this.port          = port;
		this.auth          = true;
		this.username      = username;
		this.password      = password;
	}
	
	public CassandraConfig(String contactPoints, Integer port, String username, String password, String keyspace) {
		this.contactPoints = contactPoints;
		this.port          = port;
		this.auth          = true;
		this.username      = username;
		this.password      = password;
		this.keyspace      = keyspace;
	}

	public String getContactPoints() {
		return contactPoints;
	}

	public void setContactPoints(String contactPoints) {
		this.contactPoints = contactPoints;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean auth) {
		this.auth = auth;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public int getPoolMaxTotal() {
		return poolMaxTotal;
	}

	public void setPoolMaxTotal(int poolMaxTotal) {
		this.poolMaxTotal = poolMaxTotal;
	}

	public int getPoolMaxIdle() {
		return poolMaxIdle;
	}

	public void setPoolMaxIdle(int poolMaxIdle) {
		this.poolMaxIdle = poolMaxIdle;
	}

	public int getPoolMinIdle() {
		return poolMinIdle;
	}

	public void setPoolMinIdle(int poolMinIdle) {
		this.poolMinIdle = poolMinIdle;
	}

	public long getPoolMaxWaitMillis() {
		return poolMaxWaitMillis;
	}

	public void setPoolMaxWaitMillis(long poolMaxWaitMillis) {
		this.poolMaxWaitMillis = poolMaxWaitMillis;
	}
}
