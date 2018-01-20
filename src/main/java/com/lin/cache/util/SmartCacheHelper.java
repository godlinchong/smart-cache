package com.lin.cache.util;


import com.lin.cache.constant.Constant;
import com.lin.cache.enums.CacheTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

 /**
 * 类名称:读取配置文件并保管配置参数的辅助工具<br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public class SmartCacheHelper {
	private static final Logger LOG = LoggerFactory.getLogger(SmartCacheUtils.class);
	private String appName = Constant.DEFAULT_APP_NAME;
	private String group = Constant.DEFAULT_GROUP;
	private CacheTypeEnum cache_L1 = null;
	private CacheTypeEnum cache_L2 = null;
	private Properties prop = null;
	
	// 单例
	private static SmartCacheHelper smartCacheHelper = new SmartCacheHelper();
	
	public static SmartCacheHelper getInstance(){
		return smartCacheHelper;
	}
	
	private SmartCacheHelper(){
		if(prop==null){
			try {
				InputStream is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(Constant.CACHE_CONFIG_PATH);
				prop = new Properties();
				prop.load(is);
				appName = StringUtils.isBlank(prop.getProperty("appName")) ? Constant.DEFAULT_APP_NAME : prop.getProperty("appName");
				group = StringUtils.isBlank(prop.getProperty("group")) ? Constant.DEFAULT_GROUP : prop.getProperty("group");
				
				try {
					cache_L1 = CacheTypeEnum.valueOf(prop.getProperty("cache_L1"));
				} catch (Exception e) {
					throw new RuntimeException("cache type of level 1 not correctly specified!");
				}
				
				
				try {
					cache_L2 = CacheTypeEnum.valueOf(prop.getProperty("cache_L2"));
				} catch (Exception e) {
					cache_L2 = null;					// 二级缓存没指定则只使用一级缓存
					LOG.warn("cache type of level 2 not correctly configured! Will not use the level 2 cache. ");
				}
				
			} catch (Exception e) {
				throw new RuntimeException("loading configuration from config file:" + Constant.CACHE_CONFIG_PATH + " failed!", e);
			}
		}
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	

	public CacheTypeEnum getCache_L1() {
		return cache_L1;
	}

	public void setCache_L1(CacheTypeEnum cache_L1) {
		this.cache_L1 = cache_L1;
	}

	public CacheTypeEnum getCache_L2() {
		return cache_L2;
	}

	public void setCache_L2(CacheTypeEnum cache_L2) {
		this.cache_L2 = cache_L2;
	}
	
}
