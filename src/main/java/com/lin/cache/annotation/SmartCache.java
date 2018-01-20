/** 
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.lin.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 类名称: 缓存注解<br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmartCache {

	/**
	 * 所属分组
	 * 
	 * @return
	 */
	public String group() default "";

	/**
	 * 缓存的key
	 * 
	 * @return
	 */
	public String key();
	
	/**
	 * 有效时间
	 * @return
	 */
	public int expire() default 3600;

	/**
	 * 添加还是删除，默认是添加
	 * 
	 * @return
	 */
	public boolean remove() default false;

}
