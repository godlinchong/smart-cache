package com.lin.cache.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.util.ReflectionUtils;

/**
 * 类处理工具
 * @author lin
 */
public class ClassUtils {
	
	/**
	 * 在类或父类和接口上的方法查找注解</br>
	 * 优先级依次为当前类,实现类,接口
	 * @param clazz 类
	 * @param methodName 方法名称
	 * @param paramTypes 参数类型
	 * @return
	 */
	public static <T extends Annotation> Method findAnnotation(Class<T> annotationClass, Class<?> clazz, String methodName, Class<?>... paramTypes) {
		T annotation = null;
		
		Method method = findMethod(clazz, methodName, paramTypes);
		if (method != null) {
			annotation = method.getAnnotation(annotationClass);
		}
		
		// 如果没找到annotation,则继续在接口上查找
		if (annotation == null && !clazz.isInterface()) {
			
		}
		
		return null;
	}
	
	/**
	 * 从自身以及父类上查找所有接口
	 * @param clazz
	 * @return
	 */
	public static Class<?> getAllInterface(Class<?> clazz) {
		HashSet<Class<?>> interfaces = new HashSet<Class<?>>();
		Class<?> searchType = clazz;
		while (searchType != null) {
			interfaces.addAll(Arrays.asList(searchType.getInterfaces()));
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * 查找真正的实现方法的Method
	 * @param method method
	 * @param targetClass 接口实现类
	 * @return 实现方法的Method实例或null
	 */
	public static Method getImplementMethod(Method method, Class<?> targetClass) {
		AssertUtils.notNull(method, "method不能为null");
		AssertUtils.notNull(targetClass, "targetClass不能为null");
		AssertUtils.isTrue(method.getDeclaringClass().isAssignableFrom(targetClass), "method不是targetClass的成员方法");
		
		Method implementMethod = null;
		if (!targetClass.equals(method.getDeclaringClass())) {
			implementMethod = findMethod(targetClass, method.getName(), method.getParameterTypes());
		} else {
			implementMethod = method;
		}
		return implementMethod;
	}
	
	/**
	 * 根据方法名和参数类型,搜索类的Method对象,
	 * 如果找不到就再到父类去找，直到找到为止。
	 * @param clazz
	 * @param name 方法名称
	 * @param paramTypes 参数类型,元素的顺序影响查询结果
	 * @return Method对象或者为null
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		AssertUtils.notNull(clazz, "clazz不能为null");
		AssertUtils.notNull(name, "name不能为null");
		
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())
						&& (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static void main(String[] args) {
		Class<?>[] a = ScheduledThreadPoolExecutor.class.getInterfaces();
		a.toString();
	}
}
