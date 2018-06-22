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
public @interface UseCase {
    int TRAMPOLINE = 0;
    int IO = 1;
    int COMPUTATION = 2;
    int SINGLE = 3;
    int NEW_THREAD = 4;
    int MAIN_THREAD = 5;

    int executeThread() default IO;

    int postExecutionThread() default MAIN_THREAD;

    String remoteClassName() default "";

    String localClassName() default "";

    String memoryClassName() default "";

    String name() default "";
}
