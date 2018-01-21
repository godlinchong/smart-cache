package com.lin;


import com.lin.cache.service.CacheProvider;
import com.lin.cache.service.impl.RedisCacheProvider;
import com.lin.cache.test.MyTest;
import com.lin.cache.test.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import javax.annotation.Resource;


/**
 * Unit test for simple App.
 */

public class AppTest extends BaseJunit4Test {
    private static Logger logger= LoggerFactory.getLogger(AppTest.class);
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
    @Test
    public void testObject(){
        Person person = new Person();
        person.setAge(12);
        test.setObject("zouyu1");
        CacheProvider cacheProvider = new RedisCacheProvider();
        Object key1 = cacheProvider.get("zouyu1");
        System.out.println(key1);
    }
    @Test
    public void test1(){
        logger.info("we23423");
        System.out.println("wee");
    }

}
