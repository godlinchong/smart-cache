package com.lin.cache;


import com.lin.cache.constant.Constant;
import com.lin.cache.enums.CacheTypeEnum;
import com.lin.cache.service.CacheProvider;
import com.lin.cache.service.impl.EhCacheProvider;
import com.lin.cache.service.impl.MemcachedProvider;
import com.lin.cache.service.impl.RedisCacheProvider;
import com.lin.cache.util.SmartCacheHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 缓存管理类
 * @author：linchong
 * @since：2017-09-09
 * @version:1.0
 */
public class SmartCacheManager {

    private static final Log LOG = LogFactory.getLog(SmartCacheManager.class);

    // 保存了每种缓存提供类的唯一实例
    private static Map<CacheTypeEnum, CacheProvider> services = new ConcurrentHashMap<CacheTypeEnum, CacheProvider>();

    private static String group = Constant.DEFAULT_GROUP;
    private static CacheTypeEnum cache_L1;
    private static CacheTypeEnum cache_L2;

    // singleton
    private static SmartCacheHelper smartCacheHelper = SmartCacheHelper.getInstance();
    private static SmartCacheManager smartCacheManager = new SmartCacheManager();


    private SmartCacheManager(){
        cache_L1 = smartCacheHelper.getCache_L1();
        cache_L2 = smartCacheHelper.getCache_L2();
    }

    public synchronized static SmartCacheManager getInstance() {
        return smartCacheManager;
    }



    /**
     * 获取对应的缓存服务，没有则新创建并初始化
     * @param cacheType
     * @return
     */
    public synchronized static CacheProvider getService(CacheTypeEnum cacheType){

        if(cacheType == null){ return null; }

        if( !services.containsKey(cacheType) ){
            CacheProvider cacheService = null;
            switch(cacheType){
                case EHCACHE :
                    cacheService = new EhCacheProvider();
                    break;
                case MEMCACHED :
                    cacheService = new MemcachedProvider();
                    break;
                case REDIS :
                    cacheService = new RedisCacheProvider();
                    break;
                default : 	// default
                    throw new RuntimeException("unsupported cache type encountered："+cacheType);
            }

            services.put(cacheType, cacheService);
        }

        return services.get(cacheType);
    }



    public Object get(String key, CacheTypeEnum cacheType) {

        if(cacheType != null) {
            return getService(cacheType).get(key);
        } else{

            Object obj = getService(cache_L1).get(key);
            if( obj == null && cache_L2 != null ){
                obj = getService(cache_L2).get(key);
                if(obj != null) {
                    LOG.debug("get from cache level 2:" + cache_L2 + " of key:" + key);
                }
            }else{
                LOG.debug("get from cache level 1:" + cache_L1 + " of key:" + key);
            }

            return obj;
        }

    }

    public Object get(String group, String key, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            return getService(cacheType).get(group, key);
        }else {

            Object obj = getService(cache_L1).get(group, key);
            if( obj == null && cache_L2 != null ){
                obj = getService(cache_L2).get(group, key);
                if(obj != null) {
                    LOG.debug("get from cache level 2:" + cache_L2 + " of key:" + key);
                }
            }else{
                LOG.debug("get from cache level 1:" + cache_L1 + " of key:" + key);
            }

            return obj;
        }

    }
    public Object get(String appName, String group, String key, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            return getService(cacheType).get(appName, group, key);
        }else {

            Object obj = getService(cache_L1).get(appName, group, key);
            if( obj == null && cache_L2 != null ){
                obj = getService(cache_L2).get(appName, group, key);
                if(obj != null) {
                    LOG.debug("get from cache level 2:" + cache_L2 + " of key:" + key);
                }
            }else{
                LOG.debug("get from cache level 1:" + cache_L1 + " of key:" + key);
            }

            return obj;
        }

    }


    public void remove(String key, CacheTypeEnum cacheType) {

        if(cacheType != null){
            getService(cacheType).remove(key);
        }else{

            getService(cache_L1).remove(key);
            if(cache_L2!=null) {
                getService(cache_L2).remove(key);
            }

        }

    }

    public void remove(String group, String key, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            getService(cacheType).remove(group, key);
        }else{

            getService(cache_L1).remove(group, key);
            if(cache_L2!=null) {
                getService(cache_L2).remove(group, key);
            }
        }

    }
    public void remove(String appName, String group, String key, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            getService(cacheType).remove(appName, group, key);
        }else{

            getService(cache_L1).remove(appName, group, key);
            if(cache_L2!=null) {
                getService(cache_L2).remove(appName, group, key);
            }
        }

    }



    public void set(String key, Object val, int seconds, CacheTypeEnum cacheType) {

        if(cacheType != null) {
            getService(cacheType).set(key, val, seconds);
        }else{

            getService(cache_L1).set(key, val, seconds);
            if(cache_L2!=null) {
                getService(cache_L2).set(key, val, seconds);
            }

        }

    }

    public void set(String group, String key, Object val, int seconds, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            getService(cacheType).set(group, key, val, seconds);
        }else{

            getService(cache_L1).set(group, key, val, seconds);
            if(cache_L2 != null) {
                getService(cache_L2).set(group, key, val, seconds);
            }
        }

    }
    public void set(String appName, String group, String key, Object val, int seconds, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            getService(cacheType).set(appName, group, key, val, seconds);
        }else{

            getService(cache_L1).set(appName, group, key, val, seconds);
            if(cache_L2 != null) {
                getService(cache_L2).set(appName, group, key, val, seconds);
            }
        }

    }



    public void clear(CacheTypeEnum cacheType) {

        if(cacheType != null) {
            getService(cacheType).clear();
        }else{

            getService(cache_L1).clear();
            if(cache_L2!=null) {
                getService(cache_L2).clear();
            }
        }

    }


    public void clear(String group, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            getService(cacheType).clear(group);
        }else{

            getService(cache_L1).clear(group);
            if(cache_L2!=null) {
                getService(cache_L2).clear(group);
            }
        }

    }
    public void clear(String appName, String group, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            getService(cacheType).clear(appName, group);
        }else{

            getService(cache_L1).clear(appName, group);
            if(cache_L2!=null) {
                getService(cache_L2).clear(appName, group);
            }
        }

    }


    public Set<String> keys(CacheTypeEnum cacheType) {

        if(cacheType != null) {
            return getService(cacheType).keys();
        }else {

            return getService(cache_L1).keys();
        }

    }

    public Set<String> keys(String group, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            return getService(cacheType).keys(group);
        }else {

            return getService(cache_L1).keys(group);
        }

    }
    public Set<String> keys(String appName, String group, CacheTypeEnum cacheType) {

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            return getService(cacheType).keys(appName, group);
        }else {

            return getService(cache_L1).keys(appName, group);
        }

    }



    public boolean contains(String key, CacheTypeEnum cacheType){

        if(cacheType != null) {
            return getService(cacheType).contains(key);
        }else {

            boolean b1 = getService(cache_L1).contains(key);

            boolean b2 = false;
            if(cache_L2!=null) {
                b2 = getService(cache_L2).contains(key);
            }

            return b1 || b2;
        }

    }

    public boolean contains(String group, String key, CacheTypeEnum cacheType){

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            return getService(cacheType).contains(group, key);
        }else {

            boolean b1 = getService(cache_L1).contains(group, key);

            boolean b2 = false;
            if(cache_L2!=null) {
                b2 = getService(cache_L2).contains(group, key);
            }

            return b1 || b2;
        }

    }
    public boolean contains(String appName, String group, String key, CacheTypeEnum cacheType){

        if(group == null) {
            group = SmartCacheManager.group;
        }
        if(cacheType != null) {
            return getService(cacheType).contains(appName, group, key);
        }else {

            boolean b1 = getService(cache_L1).contains(appName, group, key);

            boolean b2 = false;
            if(cache_L2!=null) {
                b2 = getService(cache_L2).contains(appName, group, key);
            }

            return b1 || b2;
        }

    }


}
