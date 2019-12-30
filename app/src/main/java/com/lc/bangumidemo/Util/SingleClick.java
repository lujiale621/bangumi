package com.lc.bangumidemo.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface SingleClick {
    /* 点击间隔时间 */
    long value() default 10000;
}