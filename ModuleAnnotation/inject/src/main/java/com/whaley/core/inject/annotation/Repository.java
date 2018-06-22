package com.whaley.core.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yangzhi on 16/8/5.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
    int REMOTE = 0;
    int LOCAL = 1;
    int MEMORY = 2;

    int type() default MEMORY;

    String name() default "";
}
