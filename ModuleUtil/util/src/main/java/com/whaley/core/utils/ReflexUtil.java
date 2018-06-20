package com.whaley.core.utils;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

/**
 * Created by yangzhi on 16/8/12.
 */
public class ReflexUtil {

    public static Object getField(String className, Object object, String fieldName) {
        Field field = null;
        try {
            field = Class.forName(className).getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(object);
            }
        } catch (ClassNotFoundException e) {
            Logger.e(e, "getField");
        } catch (NoSuchFieldException e) {
            Logger.e(e, "getField");
        } catch (IllegalAccessException e) {
            Logger.e(e, "getField");
        }
        return null;
    }


    public static void setField(String className, Object object, String fieldName,Object value) {
        Field field = null;
        try {
            field = Class.forName(className).getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(object,value);
            }
        } catch (ClassNotFoundException e) {
            Logger.e(e, "setField");
        } catch (NoSuchFieldException e) {
            Logger.e(e, "setField");
        } catch (IllegalAccessException e) {
            Logger.e(e, "setField");
        }
    }
}
