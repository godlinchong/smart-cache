package com.lin.cache.service.impl;

import com.lin.cache.service.CacheProvider;
import com.lin.cache.service.impl.AbstractCacheProvider;

//package com.lin.cache.service.impl;
//
//import com.lin.cache.service.CacheProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Set;
//
//
///**
// *
// * @author anan.hong
// * @version 1.0
// * @Date：2015年12月8日
// * @Description
// * 		memcached缓存服务，使用alisoft的memcached-client-forjava实现，实现了cluster
// * 的多个客户端间通过异步通信的方式实现的数据同步，从而实现memcached多写。
// * 		默认同一个appName和group映射到同一个client上，通过appName和group名的连接串做hash
// * 计算映射到某个client上,即：(appName+group).hashCode() % (caches.length);
// *
// */
public class MemcachedProvider extends AbstractCacheProvider implements CacheProvider {
//
//	private static final Logger LOG = LoggerFactory.getLogger(RedisCacheProvider.class);
//
//	private static MemcachedCacheManager manager;
//
//
//	public MemcachedProvider() {
//
//		initCacheManager();
//	}
//
//	public synchronized void initCacheManager(){
//		if(manager==null){
//			manager = (MemcachedCacheManager) CacheUtil.getCacheManager(IMemcachedCache.class,
//					MemcachedCacheManager.class.getName());
//			manager.setConfigFile("runtimecfg/memcached.xml");
//			// manager.setResponseStatInterval(5*1000);
//			manager.start();
//		}
//	}
//
//	// TODO 如果有client停机断开怎么办，manager是否有对cachePool中的资源心跳测试？
//	// 可能hash计算拿出来的cache客户端已经dead了，目前没任何处理
//	// 根据appName和group的hash值映射到memcached缓存池的某个缓存client上
//	private IMemcachedCache getCache(String appName, String group){
//
//		String[] caches = manager.getCachepool().keySet().toArray(new String[0]);
//
//		if( caches.length <= 0 ) {
//			throw new RuntimeException("no memcached client alived!");
//		}
//
//		int index = (appName+group).hashCode() % (caches.length);
//		index = Math.abs(index);
//
//		String cacheName = caches[index];
//
//		LOG.debug("use cache：" + cacheName);
//
//		IMemcachedCache cache = manager.getCachepool().get(cacheName);
//
//		return cache;
//	}
//
//
//	@Override
//	public Object get(String appName, String group, String key) {
//		Object obj = getCache(appName, group).get(key);
//		if(obj!=null){
//			return obj;
//		}
//		return null;
//	}
//
//	@Override
//	public Boolean remove(String appName, String group, String key) {
//		getCache(appName, group).remove(key);
//		return true;
//	}
//
//
//	@Override
//	public String set(String appName, String group, String key, Object val, int seconds) {
//		if(seconds > 0){
//			getCache(appName, group).put(key, val, seconds).toString();
//		}else{
//			getCache(appName, group).put(key, val);
//		}
//		return key;
//	}
//
//	@Override
//	public Long clear(String appName, String group) {
//		IMemcachedCache cache = getCache(appName, group);
//		cache.clear();
//		return 0L;
//	}
//
//	/**
//	 * 获取所有的缓存键，memcached无法实现按组区分
//	 */
//	@Override
//	public Set<String> keys(String appName, String group) {
//		return getCache(appName, group).keySet();
//	}
//
//
//	@Override
//	public boolean contains(String appName, String group, String key) {
//		return getCache(appName, group).containsKey(key);
//	}
//
//	/**
//	 * 停止缓存管理器
//	 */
//	public void destory() {
//		if (manager != null) {
//			manager.stop();
//		}
//	}
//
}

