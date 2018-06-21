package com.whaley.core.repository;

import com.google.common.base.Preconditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by YangZhi on 2017/6/29 15:26.
 */

public class LocalRepository implements IRepository {

    private static final class Holder{
        private static final LocalRepository instance = new LocalRepository();
    }

    public static LocalRepository getInstance() {
        return LocalRepository.Holder.instance;
    }

    private LocalRepository() {
    }

    @Override
    public <T> T obtainService(Class<T> remoteService) {
        Preconditions.checkNotNull(remoteService);
        try {
            Object instance;
            Method method = remoteService.getMethod("getInstance");
            if(method!=null) {
                instance = method.invoke(null);
                if(instance !=null && instance.getClass().getName().equals(remoteService.getName())) {
                    return (T) instance;
                }
            }
            instance = remoteService.newInstance();
            Preconditions.checkNotNull(instance);
            return (T) instance;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

}
