package com.github.jinai6682039.livedata_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * createTime: 2018/9/27
 * by hanxu17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ObserverModel {
   String value() default "";
}
