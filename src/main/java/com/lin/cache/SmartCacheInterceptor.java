/** 
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.lin.cache;

import java.lang.reflect.Method;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.lin.cache.annotation.SmartCache;
import com.lin.cache.util.SerializeUtils;
import com.lin.cache.util.SmartCacheHelper;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;


/**
 * 缓存拦截器
 * @author：linchong
 * @since：2017-09-09
 * @version:1.0
 */
public class SmartCacheInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtils.class);

	private static ScriptEngineManager manager = new ScriptEngineManager();
	
	private static SmartCacheManager smartCacheManager = SmartCacheManager.getInstance();
	private static SmartCacheHelper smartCacheHelper = SmartCacheHelper.getInstance();
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept
	 * .MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		SmartCache smartCache = AnnotationUtils.findAnnotation(
				invocation.getMethod(), SmartCache.class);
		if (smartCache == null) {
			Class<?> targetClass = invocation.getThis() != null ? AopUtils
					.getTargetClass(invocation.getThis()) : null;
			Method method = AopUtils.getMostSpecificMethod(
					invocation.getMethod(), targetClass);
			smartCache = AnnotationUtils.findAnnotation(method,
					SmartCache.class);
			if (null == smartCache) {
				return invocation.proceed();
			}
		}
		String localGroup = StringUtils.isBlank(smartCache.group()) ? smartCacheHelper.getGroup()
				: smartCache.group();
		String key = getSelDefKey(smartCache.key(), invocation.getArguments());

		if (smartCache.remove()) {
			// 删除缓存数据
			smartCacheManager.remove(localGroup, key, null);
			return invocation.proceed();
		} else {
			// 处理缓存
			return handleCache(invocation, localGroup, key, smartCache.expire());
		}

	}

	/**
	 * 处理缓存
	 * 
	 * @param invocation
	 * @param localGroup
	 * @param key
	 * @return
	 */
	private Object handleCache(MethodInvocation invocation, String localGroup, String key, int TTL)
			throws Throwable {
		Object obj = smartCacheManager.get(localGroup, key, null);
		if (obj != null) {
			return obj;
		} else {
			Object result = null;
			try {
				result = invocation.proceed();
			} catch (Throwable e) {
				throw e;
			}
			smartCacheManager.set(localGroup, key, result, TTL, null);
			return result;
		}
	}

	/**
	 * 获取自定义Key
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	private String getSelDefKey(String key, Object[] args) {
		ScriptEngine engine = manager.getEngineByName("js");
		engine.put("args", args);
		try {
			return (String) engine.eval(key);
		} catch (ScriptException e) {
			LOGGER.debug("fail to eval express", e);
			throw new RuntimeException("fail to eval express", e);
		}
	}



}
