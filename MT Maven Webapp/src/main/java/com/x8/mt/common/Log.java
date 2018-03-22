package com.x8.mt.common;

import java.lang.annotation.*;

/**
 * 
 * 作者:allen--
 * 时间:2017年11月24日
 * 作用:自定义注解---
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface Log {

    /** 要执行的操作类型比如：add操作 **/  
    public String operationType() default "";  
     
    /** 要执行的具体操作比如：添加用户,包含操作影响的总记录数 **/  
    public String operationDesc() default "";
}