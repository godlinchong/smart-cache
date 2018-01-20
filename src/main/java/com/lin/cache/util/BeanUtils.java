package com.lin.cache.util;

import java.beans.Beans;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * bean工具
 * @author lin
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanUtils{

	/**
	 * 将source的所有对象copy到desc中
	 * @param source 原对象列表
	 * @param desc 目标对象列表
	 * @param descClazz 目标对象类型
	 */
	public static void copyListProperties(List source, List desc, Class descClazz) {
		for (Object o : source) {
			try {
				Object d = descClazz.newInstance();
				copyProperties(o, d);
				desc.add(d);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void copyProperties(Object source, Object target) {
		String[] properties = getPropertyNames(source);
		copyProperties(source, target, properties, true, true);
	}

	public static void copyProperties(Object source, Object target,
			String[] properties) {
		copyProperties(source, target, properties, true, true);
	}
	
	public static void copyProperties(Object source, Object target, boolean convertType, boolean ignoreNullValue) {
		String[] properties = getPropertyNames(source);
		copyProperties(source, target, properties, convertType, ignoreNullValue);
	}

	public static void copyProperties(Object source, Object target,
			String[] properties, boolean convertType, boolean ignoreNullValue) {
		Map valueMap = getProperties(source, properties);
		
		Iterator keys = valueMap.keySet().iterator();
		while (keys.hasNext()) {
			String property = keys.next().toString();
			Object value = valueMap.get(property);
			copyProperty(target, property, value, convertType, ignoreNullValue);
		}
	}

	public static boolean copyProperty(Object obj, String property, Object value) {
		return copyProperty(obj, property, value, true, true);
	}

	public static boolean copyProperty(Object obj, String property,
			Object value, boolean convertType, boolean ignoreNullValue) {
		if (obj == null) {
			throw new IllegalArgumentException("no bean specified");
		}
		if (property == null) {
			throw new IllegalArgumentException("no property specified");
		}
		if (ignoreNullValue && value == null) {
			return true;
		}

		if (obj instanceof Map) {
			return invokeSetter(obj, property, value, convertType,
					ignoreNullValue);
		}

		StringTokenizer st = new StringTokenizer(property, ".");
		if (st.countTokens() == 0) {
			return false;
		}

		Object current = obj;
		try {
			while(st.hasMoreTokens()){
				String currentPropertyName = st.nextToken();
				if(st.hasMoreTokens()){
					current = invokeGetter(current, currentPropertyName);
				}else {
					try {
						invokeSetter(current, currentPropertyName, value,
								convertType, ignoreNullValue);
					} catch (Exception e) {
						return false;
					}
				}
			}
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static Object getIndexedProperty(Object obj, int index) {
		if (!obj.getClass().isArray()) {
			if (!(obj instanceof java.util.List)) {
				throw new IllegalArgumentException("bean is not indexed");
			} else {
				return ((java.util.List) obj).get(index);
			}
		} else {
			return (Array.get(obj, index));
		}
	}

	/**
	 * 
	 * @param obj
	 * @param property
	 * @return
	 */
	public static Object getProperty(Object obj, String property) {
		if (obj == null) {
			throw new IllegalArgumentException("no bean specified");
		}
		if (property == null) {
			throw new IllegalArgumentException("no property specified");
		}

		if (obj instanceof Map) {
			return ((Map) obj).get(property);
		}

		StringTokenizer st = new StringTokenizer(property, ".");
		if (st.countTokens() == 0) {
			return null;
		}

		Object result = obj;

		try {
			while (st.hasMoreTokens()) {
				String currentPropertyName = st.nextToken();
				if (result != null) {
					result = PropertyUtils.getProperty(result,
							currentPropertyName);
				}
			}
			return result;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对象属性
	 * @param obj
	 * @param properties
	 * @return
	 */
	public static Map getProperties(Object obj, String[] properties) {
		if (obj == null) {
			throw new IllegalArgumentException("no bean specified");
		}
		if (properties == null) {
			throw new IllegalArgumentException("no priperties specified");
		}
		Map result = new LinkedHashMap();
		for (int i = 0; i < properties.length; i++) {
			Object value = getProperty(obj, properties[i]);
			result.put(properties[i], value);
		}
		return result;
	}

	/**
	 * 获取对象的所有属性
	 * @param obj
	 * @return
	 */
	public static Map getProperties(Object obj) {
		String[] propertiyNames = getPropertyNames(obj);
		return getProperties(obj, propertiyNames);
	}

	
	/**
	 * get property from object
	 * @param obj target object
	 * @param property target property
	 * @return
	 */
	public static Object invokeGetter(Object obj, String property) {
		if (obj instanceof Map) {
			return ((Map) obj).get(property);
		} else {
			return PropertyUtils.getProperty(obj, property);
		}
	}

	/**
	 * inject value into object
	 * @param obj target object 
	 * @param property target property 
	 * @param value injected object
	 * @param autoConvert 是否需要自动转换类型 
	 * @param ingoreNullValue 是否自动忽略NULL值
	 * @return
	 */
	public static boolean invokeSetter(Object target, String property,
			Object value, boolean autoConvert, boolean ingoreNullValue) {
		if (target instanceof Map) {
			((Map) target).put(property, value);
			return true;
		}
		Object newValue = null;
		if (autoConvert) {
			Class type = PropertyUtils
					.getPropertyType(target.getClass(), property);
			if (Beans.isInstanceOf(value, type)) {
				newValue = value;
			} else if (value instanceof String) {
				newValue = ConvertUtils.convert(type, (String) value);
			} else {
				newValue = value;
			}
		} else {
			newValue = value;
		}
		if (!ingoreNullValue || newValue != null) {
			boolean r = PropertyUtils.setProperty(target, property, newValue);
		}
		return true;
	}
	

	/**
	 * 获取所有属性名称
	 * @param obj
	 * @return
	 */
	private static String[] getPropertyNames(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException("no bean specified");
		}
		if (obj instanceof Map) {
			Object[] keys = ((Map) obj).keySet().toArray();
			String[] results = new String[keys.length];
			for (int i = 0; i < keys.length; i++) {
				results[i] = keys[i] + "";
			}
			return results;
		}
		String[] result = PropertyUtils.getPropertyNames(obj.getClass());
		int index = Arrays.binarySearch(result, "class");

		if(index>0){
			if(result.length==1){
				return new String[0];
			}
			String[] newResult = new String[result.length-1];
			System.arraycopy(result,0,newResult,0,index);
			if(index<result.length){
				System.arraycopy(result,index+1,newResult,index,result.length-index-1);
			}
			return newResult;
		}else{
			return result;
		}
	}
}
