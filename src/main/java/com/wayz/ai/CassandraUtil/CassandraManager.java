package com.wayz.ai.CassandraUtil;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class CassandraManager implements InitializingBean, DisposableBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		CassandraSessionPool.initSessionPool();
	}
	
	@Override
	public void destroy() throws Exception {
		CassandraSessionPool.getInstance().close();
	}
}
