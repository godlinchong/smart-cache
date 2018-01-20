/** 
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.lin.cache.util;


import com.lin.cache.SmartCacheManager;
import com.lin.cache.enums.CacheTypeEnum;

import java.util.Set;



/**
 * 类名称: 缓存工具 <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public class SmartCacheUtils {

	/** 缓存服务提供者的统一管理器 */
	private static final SmartCacheManager smartCacheManager = SmartCacheManager.getInstance();
	
	
	/**
	 * 从默认分组中获取缓存对象
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return smartCacheManager.get(key, null);
	}
	
	/**
	 * 从指定分组中获取缓存对象
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @return
	 */
	public static Object get(String group, String key) {
		return smartCacheManager.get(group, key, null);
	}
	
	/**
	 * 从指定分组中获取缓存对象，并指定缓存种类
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @param cacheType
	 * @return
	 */
	public static Object get(String group, String key, CacheTypeEnum cacheType) {
		return smartCacheManager.get(group, key, cacheType);
	}
	public static Object get(String appName, String group, String key, CacheTypeEnum cacheType) {
		return smartCacheManager.get(appName, group, key, cacheType);
	}

	/**
	 * 从默认分组中移除缓存对象
	 * @param key
	 */
	public static void remove(String key) {
		smartCacheManager.remove(key, null);
	}
	
	/**
	 * 从指定分组中移除缓存对象
	 * @param group group为null时将使用默认分组
	 * @param key
	 */
	public static void remove(String group, String key) {
		smartCacheManager.remove(group, key, null);
	}
	
	/**
	 * 从指定分组中移除缓存对象，使用指定的缓存类型
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @param cacheType 指明操作的缓存类型
	 */
	public static void remove(String group, String key, CacheTypeEnum cacheType) {
		smartCacheManager.remove(group, key, cacheType);
	}
	public static void remove(String appName, String group, String key, CacheTypeEnum cacheType) {
		smartCacheManager.remove(appName, group, key, cacheType);
	}

	/**
	 * 往默认分组中添加缓存对象，并设置超时时间seconds
	 * @param key
	 * @param val 		默认二进制序列化时只支持Serializable的子类
	 * @param seconds 	超时时间，-1表示永久有效
	 */
	public static void set(String key, Object val, int seconds){
		smartCacheManager.set(key, val, seconds, null);
	}
	
	/**
	 * 往指定分组中添加缓存对象，并设置超时时间seconds
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @param val 		默认二进制序列化时只支持Serializable的子类
	 * @param seconds 	超时时间，-1表示永久有效
	 */
	public static void set(String group, String key, Object val, int seconds){
		smartCacheManager.set(group, key, val, seconds, null);
	}

	/**
	 * 往指定分组中添加缓存对象，并设置超时时间seconds
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @param val 		默认二进制序列化时只支持Serializable的子类
	 * @param seconds 	超时时间，-1表示永久有效
	 * @param cacheType
	 */
	public static void set(String group, String key, Object val, int seconds, CacheTypeEnum cacheType) {
		smartCacheManager.set(group, key, val, seconds, cacheType);
	}
	public static void set(String appName, String group, String key, Object val, int seconds, CacheTypeEnum cacheType) {
		smartCacheManager.set(appName, group, key, val, seconds, cacheType);
	}
	
	/**
	 * 清空【默认分组】中的缓存对象
	 * 注意：不是清空所有缓存对象
	 */
	public static void clear(){
		smartCacheManager.clear(null);
	}
	
	/**
	 * 清空指定分组中的缓存对象
	 * @param group group为null时将使用默认分组
	 */
	public static void clear(String group){
		smartCacheManager.clear(group, null);
	}
	
	/**
	 * 清空指定缓存类型的指定分组中的缓存对象
	 * @param group group为null时将使用默认分组
	 * @param cacheType
	 */
	public static void clear(String group, CacheTypeEnum cacheType){
		smartCacheManager.clear(group, cacheType);
	}
	public static void clear(String appName, String group, CacheTypeEnum cacheType){
		smartCacheManager.clear(appName, group, cacheType);
	}
	
	/**
	 * 返回【默认分组】中的所有缓存键
	 * 注意：不是所有分组
	 * @return
	 */
	public static Set<String> keys(){
		return smartCacheManager.keys(null);
	}
	
	/**
	 * 返回指定分组中的所有缓存键
	 * @param group group为null时将使用默认分组
	 * @return
	 */
	public static Set<String> keys(String group){
		return smartCacheManager.keys(group, null);
	}
	
	/**
	 * 返回指定缓存种类的指定分组中的所有缓存键
	 * @param group group为null时将使用默认分组
	 * @param cacheType
	 * @return
	 */
	public static Set<String> keys(String group, CacheTypeEnum cacheType){
		return smartCacheManager.keys(group, cacheType);
	}
	public static Set<String> keys(String appName, String group, CacheTypeEnum cacheType){
		return smartCacheManager.keys(appName, group, cacheType);
	}
	
	/**
	 * 【默认分组】是否包含键值为key的缓存对象
	 * @param key
	 * @return
	 */
	public static boolean contains(String key){
		return smartCacheManager.contains(key, null);
	}
	
	/**
	 * 指定分组中是否包含键值为key的缓存对象
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @return
	 */
	public static boolean contains(String group, String key){
		return smartCacheManager.contains(group, key, null);
	}
	
	/**
	 * 指定分组中是否包含键值为key的缓存对象
	 * @param group group为null时将使用默认分组
	 * @param key
	 * @param cacheType
	 * @return
	 */
	public static boolean contains(String group, String key, CacheTypeEnum cacheType){
		return smartCacheManager.contains(group, key, cacheType);
	}
	public static boolean contains(String appName, String group, String key, CacheTypeEnum cacheType){
		return smartCacheManager.contains(appName, group, key, cacheType);
	}
	

}
