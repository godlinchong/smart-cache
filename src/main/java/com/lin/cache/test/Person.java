/**
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay)
 */
package com.lin.cache.test;

import java.io.Serializable;

/**
 * 类名称: Person <br>
 * 类描述: <br>
 *
 * @author: chong.lin
 * @date: 2018/1/20 下午7:52
 * @company: 易宝支付(YeePay)
 */
public class Person implements Serializable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}