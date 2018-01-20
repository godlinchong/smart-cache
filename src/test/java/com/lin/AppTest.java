package com.lin;


import com.lin.cache.service.CacheProvider;
import com.lin.cache.service.impl.RedisCacheProvider;
import com.lin.cache.test.MyTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import javax.annotation.Resource;


/**
 * Unit test for simple App.
 */

public class AppTest extends BaseJunit4Test {
    @Autowired
    @Qualifier(value = "mytest")
    private MyTest test;

    @Test
    public void testApp()
    {
        CacheProvider cacheProvider = new RedisCacheProvider();
        cacheProvider.set("lin","323",60);
        Object key1 = cacheProvider.get("lin");
        System.out.println(key1);
    }
    @Test
    public void test(){
        String sdsdd = test.settt("s","c");
//        System.out.println(sdsdd);
        System.out.println("=======");
        CacheProvider cacheProvider = new RedisCacheProvider();
        Object key1 = cacheProvider.get("sc");
        System.out.println(key1);

    }

}
