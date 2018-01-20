package com.lin.cache.service.impl;

import com.lin.cache.service.CacheProvider;
import com.lin.cache.util.CheckUtils;
import com.lin.cache.util.ZkClientUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 类名称: Ehcache缓存 <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public class EhCacheProvider extends AbstractCacheProvider implements CacheProvider {
	/**
	 * 创建一个简单的cacheManager
	 */
	private static CacheManager cacheManager = CacheManager.getInstance();

	/**
	 * 获取简单的Cache对象
	 * 
	 * @param appName
	 * @param group
	 * @return
	 */
	private Cache getCache(String appName, String group) {
		String cacheName = new StringBuilder(appName).append("/").append(group)
				.toString();
		Cache cache;
		synchronized (cacheManager) {
			cache = cacheManager.getCache(cacheName);
			if (null == cache) {
				cacheManager.addCache(cacheName);
				cache = cacheManager.getCache(cacheName);
			}
		}
		return cache;
	}

	/**
	 * 获取缓存
	 * 
	 * @param appName
	 * @param group
	 * @param key
	 * @return
	 */
	@Override
	public Object get(String appName, String group, String key) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		Element ele = getCache(appName, group).get(key);
		if (ele != null) {
			return ele.getObjectValue();
		}
		return null;
	}

	/**
	 * 删除缓存
	 * 
	 * @param appName
	 * @param group
	 * @param key
	 * @return 
	 */
	@Override
	public Boolean remove(String appName, String group, String key) {
		return remove(appName,group,key,true);
	}


	/**
	 * 删除缓存
	 *
	 * @param appName
	 * @param group
	 * @param key
	 * @return
	 */
	public Boolean remove(String appName, String group, String key,boolean isNotify) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		Boolean result = getCache(appName, group).remove(key);
		if(isNotify){
			ZkClientUtils.configUpdateNotify(appName, group, key);
		}
		return result;
	}


	@Override
	public Long clear(String appName, String group) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		
		getCache(appName, group).removeAll();
		ZkClientUtils.configUpdateNotify(appName, group, null);
		return Long.MAX_VALUE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> keys(String appName, String group) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		List<String> keys = getCache(appName, group).getKeys();
		return new HashSet<String>(keys);
	}

	@Override
	public String set(String appName, String group, String key, Object val,
			int seconds) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		ZkClientUtils.registerListener(appName);// 添加监听
		Element e = new Element(key, val);
		if( seconds>0 ){
			e.setTimeToLive(seconds);
		}
		getCache(appName, group).put(e);
		return null;
	}

	@Override
	public boolean contains(String appName, String group, String key) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		return getCache(appName, group).isKeyInCache(key);
	}

}
