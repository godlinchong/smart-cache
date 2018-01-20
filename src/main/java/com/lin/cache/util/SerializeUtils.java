/**
 * Company: 易宝支付(YeePay)
 * Copyright: Copyright (c)2011
 */

package com.lin.cache.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * 类名称: CheckUtils <br>
 * 类描述:校验工具类 <br>
 *
 * @author: chong.lin
 * @date: 2018/1/19 下午11:36
 * @company: 易宝支付(YeePay)
 */
public class SerializeUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(SerializeUtils.class);

	public static byte[] serialize(Object obj) {
		if(obj==null){
			return new byte[0];
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(outputStream);
			oos.writeObject(obj);
			return outputStream.toByteArray();
		} catch (IOException e) {
			LOGGER.error("serialize object fail, io exception!", e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					LOGGER.error("close ObjectOutputStream object error!", e);
				}
			}
		}

		return null;
	}

	public static Object deserialize(byte[] bytes) {
		if(bytes==null||bytes.length==0){
			return null;
		}
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(inputStream);
			return ois.readObject();
		} catch (IOException e) {
			LOGGER.error("deserialize object fail, io exception!", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("deserialize object fail, class not found!", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					LOGGER.error("close ObjectInputStream object error!", e);
				}
			}
		}
		return null;
	}

}
