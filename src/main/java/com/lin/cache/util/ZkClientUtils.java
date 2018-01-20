/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.lin.cache.util;

import com.lin.cache.SmartCacheManager;
import com.lin.cache.constant.Constant;
import com.lin.cache.enums.CacheTypeEnum;
import com.lin.cache.service.impl.EhCacheProvider;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 类名称: ZK工具类 <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public class ZkClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ZkClientUtils.class);
	
	private static Set<String> registApp = new HashSet<String>();
	/**
	 * 缓存节点
	 */
	private static final String ROOT_NODE = Constant.ZK_ROOT_NODE;
	
	private static ZkClient zkClient = null;

	static {
		init();
	}

	/**
	 * 设置锁目录
	 *
	 * @param path
	 */
	private static void createPath(String path) {
		path += "/";
		for (int index = 1; (index = path.indexOf("/", index + 1)) != -1;) {
			String node = path.substring(0, index);
			if (!zkClient.exists(node)) {
				zkClient.createPersistent(node);
			}
		}
	}
	
	public static String readPath(String path){
		return zkClient.readData(path);
	}

	/**
	 * 初始化组件
	 */
	public static void init() {
		try {
			ResourceBundle zkRb = ResourceBundle
					.getBundle("runtimecfg/zkConfig");
			zkClient = new ZkClient(zkRb.getString("zkServers").trim());
		} catch (Exception e) {
			logger.error("init zookeeper fail", e);
			throw new RuntimeException("init zookeeper fail", e);
		}
	}

	/**
	 * 通知zookeeper发生缓存信息变化
	 *
	 * @param appName
	 * @param group
	 * @param key
	 */
	public static void configUpdateNotify(String appName, String group,
			String key) {
		String appNode = String.format("%s/%s", ROOT_NODE, appName);
		createPath(appNode); // 创建应用节点
		Map<String, String> map = new HashMap<String, String>();
		map.put("group", group);
		map.put("key", key);
		zkClient.writeData(appNode, JSONObject.toJSONString(map));
	}

	/**
	 * 注册待监听的仓库
	 *
	 * @param appName
	 */
	public static void registerListener(final String appName) {
		if (registApp.contains(appName)) {
			return; // 已注册过监听，不再重复注册
		}
		String appNode = String.format("%s/%s", ROOT_NODE, appName);
		createPath(appNode); // 创建应用节点
		registApp.add(appName);
		zkClient.subscribeDataChanges(appNode, new IZkDataListener() {
			@Override
			public void handleDataDeleted(String path) throws Exception {
				if (logger.isDebugEnabled()) {
					logger.debug("appNode delete:{}", path);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public void handleDataChange(String path, Object value)
					throws Exception {
				if (logger.isDebugEnabled()) {
					logger.debug("appNode change:{}", path);
				}
				
				Map<String, String> map = JSONUtils.jsonToMap((String) value,
						String.class, String.class);
				
				if(StringUtils.isBlank(map.get("key"))){
					SmartCacheUtils.clear(map.get("group"), CacheTypeEnum.EHCACHE);
				} else{
//					SmartCacheUtils.remove(map.get("group"), map.get("key"), CacheTypeEnum.EHCACHE);
					EhCacheProvider cacheProvider=(EhCacheProvider) SmartCacheManager.getService(CacheTypeEnum.EHCACHE);
					cacheProvider.remove(appName,map.get("group"), map.get("key"), false);// 写死缓存类型为EHCACHE
				}
			}
		});
	}

}
