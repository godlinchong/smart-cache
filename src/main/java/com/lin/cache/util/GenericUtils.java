package com.lin.cache.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 范型工具类
 * @author lin
 */
@SuppressWarnings("rawtypes")
public class GenericUtils {
	
	/**
	 * 取得泛型参类型
	 * @param clazz 泛型类
	 * @param index 泛型参数索引
	 * @return 泛型类型
	 */
	public static Class getGenericClass(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length > index)) {
				return (Class) params[index];
			}
		}
		return null;
	}
	
	/**
	 * 取得第一个泛型类型
	 * @param clazz 泛型类
	 * @return 泛型类型
	 */
	public static Class getGenericClass(Class clazz) {
		return getGenericClass(clazz, 0);
	}

}
