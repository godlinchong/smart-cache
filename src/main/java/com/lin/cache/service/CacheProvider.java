package com.lin.cache.service;

import java.util.Set;


/**
 * 类名称: 缓存实现接口 <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public interface CacheProvider {

	public Object get(String key);

	public Object get(String group, String key);
	
	public Object get(String appName, String group, String key);

	public String set(String key, Object val, int seconds);

	public String set(String group, String key, Object val, int seconds);
	
	public String set(String appName, String group, String key, Object val, int seconds);

	public Boolean remove(String key);

	public Boolean remove(String group, String key);

	public Boolean remove(String appName, String group, String key);

	public Long clear();

	public Long clear(String group);

	public Long clear(String appName, String group);

	public Set<String> keys();

	public Set<String> keys(String group);

	public Set<String> keys(String appName, String group);

	public boolean contains(String key);

	public boolean contains(String group, String key);

	public boolean contains(String appName, String group, String key);

	// TODO 批量set、get、remove ?

}
