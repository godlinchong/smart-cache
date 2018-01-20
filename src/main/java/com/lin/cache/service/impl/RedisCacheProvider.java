package com.lin.cache.service.impl;

import com.lin.cache.service.CacheProvider;
import com.lin.cache.util.CheckUtils;
import com.lin.cache.util.NativeHandler;
import com.lin.cache.util.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;

import java.io.InputStream;
import java.util.*;


/**
 * 类名称: Redis缓存实现<br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
public class RedisCacheProvider extends AbstractCacheProvider implements CacheProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheProvider.class);
	private static Pool<Jedis> pool;
	private static boolean newVersion = false;
	
	
	public RedisCacheProvider(){
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("redis-conf.properties");
			Properties prop = new Properties();
			prop.load(is);
			if (prop.containsKey("mode")) {
				if ("sentinel".equals(prop.getProperty("mode"))) {
					// 初始化哨兵模式
					initSentinelsPool(prop);
				} else if ("cluster".equals(prop.getProperty("mode"))) {
					// 初始化集群模式
					initClusterPool(prop);
				}else if("standalone".equals(prop.getProperty("mode"))){
					initStandalonePool(prop);
				} else {
					// 自动检测
					autoDetect(prop);
				}
			} else {
				// 自动检测
				autoDetect(prop);
			}
			newVersion = prop.containsKey("newVersion")
					&& "true".equals(prop.get("newVersion")) ? true : false;
		} catch (Exception e) {
			LOGGER.error("init redis pool fail", e);
			throw new RuntimeException("init redis pool fail", e);
		}
	}

	/**
	 * 自动检测,多个地址为哨兵模式，单个地址为一般连接
	 *
	 * @param prop
	 */
	private static void autoDetect(Properties prop) {
		String[] hostAndPorts = getHostAndPorts(prop);
		if (hostAndPorts.length > 1) {
			initSentinelsPool(prop);
		} else {
			initCommonPool(prop);
		}

	}

	/**
	 * 获取主机与端口号列表
	 *
	 * @param prop
	 * @return
	 */
	private static String[] getHostAndPorts(Properties prop) {
		return prop.getProperty("sentinels").trim().split(",");
	}

	/**
	 * 初始化哨兵池
	 *
	 * @param prop
	 */
	private static void initSentinelsPool(Properties prop) {
		JedisPoolConfig config = initPoolConfig(prop);
		Set<String> sentinels = new HashSet<String>(
				Arrays.asList(getHostAndPorts(prop)));
		pool = new JedisSentinelPool(prop.getProperty("masterName").trim(),
				sentinels, config, toInteger(prop, "timeout"));
	}
	/**
	 * 初始化集群池
	 *
	 * @param prop
	 */
	private static void initClusterPool(Properties prop) {
		// wait to implement
		throw new RuntimeException("un implement cluster support!");
	}

	/**
	 * 初始化哨兵池
	 *
	 * @param prop
	 */
	private static void initCommonPool(Properties prop) {
		JedisPoolConfig config = initPoolConfig(prop);
		String[] hostAndPorts = getHostAndPorts(prop);
		String[] hostAndPort = hostAndPorts[0].split(":");
		String host = hostAndPort[0];
		int port = Integer.parseInt(hostAndPort[1]);
		pool = new JedisPool(config, host, port, toInteger(prop, "timeout"));

	}
	/**
	 * 初始化单机
	 *
	 * @param prop
	 */
	private static void initStandalonePool(Properties prop) {
		JedisPoolConfig config = initPoolConfig(prop);
		String host = prop.getProperty("host");
		int port = Integer.parseInt(prop.getProperty("port"));
		pool = new JedisPool(config, host, port, toInteger(prop, "timeout"));

	}

	/**
	 * 初始化池配置
	 *
	 * @param prop
	 * @return
	 */
	private static JedisPoolConfig initPoolConfig(Properties prop) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(toInteger(prop, "maxTotal"));
		config.setMaxIdle(toInteger(prop, "maxIdle"));
		config.setMaxWaitMillis(toInteger(prop, "maxWaitMillis"));
		config.setTestOnBorrow(toBoolean(prop, "testOnBorrow"));
		config.setTestOnReturn(toBoolean(prop, "testOnReturn"));
		return config;
	}

	@Override
	public Object get(String appName, String group, String key) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		String finalKey = new StringBuilder(appName).append("/").append(group).append("/").append(key).toString();
		Object obj = null;
		try {
			obj = getObject(SafeEncoder.encode(finalKey));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if(obj==null) {
			return null;
		}
		return obj;
	}

	@Override
	public Boolean remove(String appName, String group, String key) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		String finalKey = new StringBuilder(appName).append("/").append(group).append("/").append(key).toString();
		Jedis jedis = getResource();
		try {
			return jedis.del(SafeEncoder.encode(finalKey)) > 0;
		}catch(Exception e){
			LOGGER.error("remove key: {} error!", SafeEncoder.encode(key));
			throw new RuntimeException(e);
		}finally{
			closeResource(jedis);
		}
	}


	@Override
	public String set(String appName, String group, String key, Object val, int seconds) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
//		CheckUtils.notNull(val, "val");
		String finalKey = new StringBuilder(appName).append("/").append(group).append("/").append(key).toString();
		try {
			if(seconds<=0) {
				seconds = -1;
			}
			return setObject(finalKey, seconds, val);			// 对String等类型是否经济? :与J2Cache同样
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public Long clear(String appName, String group) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		String keyword = new StringBuilder(appName).append("/").append(group).toString();
		Jedis jedis = getResource();
		try {
			Set<String> keys = jedis.keys(keyword+"*");
			if(keys.size()==0) {
				return 0L;
			}
			return jedis.del(keys.toArray(new String[0]));
		}catch(Exception e){
			LOGGER.error("return keys of: {} error!", SafeEncoder.encode(keyword));
			throw new RuntimeException(e);
		}finally{
			closeResource(jedis);
		}
	}

	@Override
	public Set<String> keys(String appName, String group) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		String keyword = new StringBuilder(appName).append("/").append(group).toString();
		Jedis jedis = getResource();
		try {
			return jedis.keys(keyword+"*");
		}catch(Exception e){
			LOGGER.error("return keys of: {} error!", SafeEncoder.encode(keyword));
			throw new RuntimeException(e);
		}finally{
			closeResource(jedis);
		}
	}

	
	@Override
	public boolean contains(String appName, String group, String key) {
		CheckUtils.notEmpty(appName, "appName");
		CheckUtils.notEmpty(group, "group");
		CheckUtils.notEmpty(key, "key");
		String finalKey = new StringBuilder(appName).append("/").append(group).append("/").append(key).toString();
		Jedis jedis = getResource();
		try {
			return jedis.exists(SafeEncoder.encode(finalKey));
		}catch(Exception e){
			LOGGER.error("execute exists for key: {} error!", SafeEncoder.encode(finalKey));
			throw new RuntimeException(e);
		}finally{
			closeResource(jedis);
		}
	}
	
	@SuppressWarnings("resource")
	public static Object getObject(byte[] key) throws Throwable {
		CheckUtils.notNull(key, "key bytes");
		Jedis jedis = null;
		try {
			jedis = getResource();

			List<byte[]> bytes = jedis.hmget(key, SafeEncoder.encode("type"), SafeEncoder.encode("value"));

			if (bytes == null) {
				return null;
			} else {
				for (byte[] aByte : bytes) {
					if (aByte == null) {
						return null;
					}
				}
			}

			Integer type = NativeHandler.decodeInteger(bytes.get(0));
			byte[] value = bytes.get(1);

			if (value == null) {
				return null;
			}

			if (8 == type) {
				return SerializeUtils.deserialize(value);
			} else {
				return NativeHandler.decode(value, type);
			}

		} catch (Throwable e) {
			LOGGER.error("get key: {} error!", SafeEncoder.encode(key));
			throw e;
		} finally {
			closeResource(jedis);
		}
	}
	
	public static String setObject(String key, int expire, Object value) throws Throwable {
		CheckUtils.notNull(key, "key");
//		CheckUtils.notNull(value, "value");

		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();

		byte[] valueBytes;
		Integer type;
		if (NativeHandler.isHandled(value)) {
			type = NativeHandler.getMarkerFlag(value);
			valueBytes = NativeHandler.encode(value);
		} else {
			type = NativeHandler.F_SERIALIZED;
			valueBytes = SerializeUtils.serialize(value);
		}

		map.put(SafeEncoder.encode("type"), NativeHandler.encode(type));
		map.put(SafeEncoder.encode("value"), valueBytes);

		return hmset(SafeEncoder.encode(key), expire, map);
	}
	
	private static String hmset(byte[] key, int expire, Map<byte[], byte[]> value) {
		Jedis jedis = getResource();
		try {
			String result = jedis.hmset(key, value);
			if (expire > 0) {
				jedis.expire(key, expire);
			}
			return result;
		} catch (Throwable e) {
			LOGGER.error("set key: {} error!", SafeEncoder.encode(key));
			return null;
		} finally {
			closeResource(jedis);
		}
	}
	
	
	public static Jedis getResource(){
		if (pool == null) {
			throw new RuntimeException("Redis Client not init!");
		}
		return pool.getResource();
	}
	
	/**
	 * 关闭资源
	 * @param jedis
	 */
	public static void closeResource(Jedis jedis) {
		if (jedis != null) {
			if (jedis.isConnected()) {
				pool.returnResource(jedis);
			} else {
				pool.returnBrokenResource(jedis);
			}
		}
	}
	
	/**
	 * 是否是新版本的redis
	 * 
	 * @return
	 */
	public static boolean isNewVersion() {
		return newVersion;
	}
	
	private static int toInteger(Properties prop, String key) {
		return Integer.parseInt(prop.getProperty(key).trim());
	}

	private static boolean toBoolean(Properties prop, String key) {
		return Boolean.valueOf(prop.getProperty(key).trim());
	}

}
