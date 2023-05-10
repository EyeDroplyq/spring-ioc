package com.lyq.springioc.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:  @Be这个注解用来讲一个对象加入到IOC容器中
 * @author: lyq
 * @createDate: 10/5/2023
 * @version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Be {
}
