/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.lin.cache.test;

import com.lin.cache.annotation.SmartCache;
import org.springframework.stereotype.Component;

/**
 * 类名称: MyTest <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午12:09
 */
@Component("mytest")
public class MyTest {
    @SmartCache(key = "args[0]+args[1]", expire = 60 * 30,remove =false)
    public String settt(String ss,String s1){
        ss = ss+":lin:"+s1;
        System.out.println(ss);
        return ss;
    }
    @SmartCache(key = "args[0]", expire = 60 * 30)
    public Person setObject(String string){
        Person person = new Person();
        person.setName(string);
        person.setAge(12);
        return person;
    }
}