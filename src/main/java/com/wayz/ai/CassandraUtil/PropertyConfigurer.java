package com.wayz.ai.CassandraUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(PropertyConfigurer.class);
	
	private static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = String.valueOf(entry.getKey());
			Object obj = entry.getValue();
			logger.info("load property: " + key + "=" + String.valueOf(obj));
			cache.put(key, obj);
		}
	}

	public static String getString(String key, String defaultValue) {
		String value = getString(key); 
		return value == null ? defaultValue : value; 
	}
	
	public static String getString(String key) {
		Object obj = cache.get(key);
		if (obj == null) {
			return null;
		}
		return String.valueOf(obj);
	}
	
	public static Integer getInteger(String key, Integer defaultValue) {
		Integer value = getInteger(key);
		return value == null ? defaultValue : value;
	}
	
	public static Integer getInteger(String key) {
		String valueStr = getString(key);
		if (StringUtils.isEmpty(valueStr)) {
			return null;
		}
		return Integer.valueOf(valueStr);
	}
	
	public static Long getLong(String key, Long defaultValue) {
		Long value = getLong(key);
		return value == null ? defaultValue : value;
	}
	
	public static Long getLong(String key) {
		String valueStr = getString(key);
		if (StringUtils.isEmpty(valueStr)) {
			return null;
		}
		return Long.valueOf(valueStr);
	}
	
	public static Float getFloat(String key, Float defaultValue) {
		Float value = getFloat(key);
		return value == null ? defaultValue : value;
	}
	
	public static Float getFloat(String key) {
		String valueStr = getString(key);
		if (StringUtils.isEmpty(valueStr)) {
			return null;
		}
		return Float.valueOf(valueStr);
	}
	
	public static Double getDouble(String key, Double defaultValue) {
		Double value = getDouble(key);
		return value == null ? defaultValue : value;
	}
	
	public static Double getDouble(String key) {
		String valueStr = getString(key);
		if (StringUtils.isEmpty(valueStr)) {
			return null;
		}
		return Double.valueOf(valueStr);
	}
	
	public static Boolean getBoolean(String key, Boolean defaultValue) {
		Boolean value = getBoolean(key); 
		return value == null ? defaultValue : value;
	}
	
	public static Boolean getBoolean(String key) {
		String valueStr = getString(key);
		if (StringUtils.isEmpty(valueStr)) {
			return null;
		}
		return Boolean.valueOf(valueStr);
	}
	
	public static Object getProperty(String key, Object defaultValue) {
		Object value = getProperty(key);
		return value == null ? defaultValue : value;
	}
	
	public static Object getProperty(String key) {
		return cache.get(key);
	}
	
	public static void setProperty(String key, Object value) {
		cache.put(key, value);
	}
}
