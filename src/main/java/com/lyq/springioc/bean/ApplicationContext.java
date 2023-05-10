package com.lyq.springioc.bean;

/**
 * @description:
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
public interface ApplicationContext {
    //这个接口就是仿照spring原生的ApplicationContext接口来写的，这个接口中有个方法来根据类返回对应的对象
     public Object getBean(Class clazz);
}
