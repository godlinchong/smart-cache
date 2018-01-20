package com.lin.cache.service.impl;

import com.lin.cache.util.SmartCacheHelper;

import java.util.Set;




/**
 * 类名称: 缓存抽象类 <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public class AbstractCacheProvider {

	private static SmartCacheHelper smartCacheHelper = SmartCacheHelper.getInstance();
	
	private static String appName;
	private static String group;
	
	public AbstractCacheProvider(){
		appName = smartCacheHelper.getAppName();
		group = smartCacheHelper.getGroup();
	}
	
	public Object get(String _key) {
		return get(appName, group, _key);
	}
	public Object get(String _group, String _key) {
		return get(appName, _group, _key);
	}
	// 被子类覆盖
	public Object get(String appName, String group, String key) {
		return null;
	}

	
	public String set(String _key, Object val, int seconds) {
		return set(appName, group, _key, val, seconds);
	}
	public String set(String _group, String _key, Object val, int seconds) {
		return set(appName, _group, _key, val, seconds);
	}
	public String set(String appName, String group, String key, Object val, int seconds) {
		return null;
	}
	
	
	public Boolean remove(String _key) {
		return remove(appName, group, _key);
	}
	public Boolean remove(String _group, String _key) {
		return remove(appName, _group, _key);
	}
	public Boolean remove(String appName, String group, String key) {
		return null;
	}


	public Long clear() {
		return clear(appName, group);
	}
	public Long clear(String _group) {
		return clear(appName, _group);
	}
	public Long clear(String appName, String group) {
		return null;
	}
	
	
	public Set<String> keys() {
		return keys(appName, group);
	}
	public Set<String> keys(String _group) {
		return keys(appName, _group);
	}
	public Set<String> keys(String appName, String group) {
		return null;
	}
	
	
	public boolean contains(String _key) {
		return contains(appName, group, _key);
	}
	public boolean contains(String _group, String _key) {
		return contains(appName, _group, _key);
	}
	public boolean contains(String appName, String group, String key) {
		return false;
	}
	
}
