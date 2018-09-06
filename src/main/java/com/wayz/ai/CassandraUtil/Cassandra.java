package com.wayz.ai.CassandraUtil;

import com.alibaba.fastjson.JSONObject;
import com.datastax.driver.core.*;
import com.datastax.driver.core.ColumnDefinitions.Definition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cassandra {
	public static final String CONTACT_POINTS = "cassandra.contact.points";
	public static final String PORT           = "cassandra.port";
	public static final String AUTH           = "cassandra.auth";
	public static final String USERNAME       = "cassandra.username";
	public static final String PASSWORD       = "cassandra.password";
	public static final String KEYSPACE       = "cassandra.keyspace";
	
	public static final String SESSION_POOL_MAX_TOTAL       = "cassandra.session.pool.max.total";
	public static final String SESSION_POOL_MAX_IDLE        = "cassandra.session.pool.max.idle";
	public static final String SESSION_POOL_MIN_IDLE        = "cassandra.session.pool.min.idle";
	public static final String SESSION_POOL_MAX_WAIT_MILLIS = "cassandra.session.pool.max.wait.millis";
	
	public void execute(Statement statement) throws Exception {
		CassandraSessionPool sessionPool = CassandraSessionPool.getInstance();
		Session              session     = null;
    	try {
    		session = sessionPool.borrowSession();
    		session.execute(statement);
		} finally {
			if (session != null) {
				sessionPool.returnSession(session);
			}
		}
	}
	
	public void executeAsync(Statement statement) throws Exception {
		CassandraSessionPool sessionPool = CassandraSessionPool.getInstance();
		Session              session     = null;
    	try {
    		session = sessionPool.borrowSession();
    		session.executeAsync(statement);
		} finally {
			if (session != null) {
				sessionPool.returnSession(session);
			}
		}
	}
	
	public List<JSONObject> query(Statement statement) throws Exception {
		CassandraSessionPool sessionPool = CassandraSessionPool.getInstance();
		Session              session     = null;
    	try {
    		session = sessionPool.borrowSession();
    		ResultSet resultSet = session.execute(statement);
    		
    		List<String>      columnNames       = new ArrayList<>();
    		ColumnDefinitions columnDefinitions = resultSet.getColumnDefinitions();
    		for (Definition definition : columnDefinitions.asList()) {
    			columnNames.add(definition.getName());
			}
    		
    		List<JSONObject> list     = new ArrayList<>();
			Iterator<Row>    iterator = resultSet.iterator();
			while(iterator.hasNext()) {
				JSONObject jsonObject = new JSONObject();
				Row        row        = iterator.next();
				for (String columnName : columnNames) {
					Object value = row.getObject(columnName);
					jsonObject.put(columnName, value);
				}
				list.add(jsonObject);
			}
    		return list;
		} finally {
			if (session != null) {
				sessionPool.returnSession(session);
			}
		}
	}
}
