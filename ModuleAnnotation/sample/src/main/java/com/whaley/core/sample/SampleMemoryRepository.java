package com.whaley.core.sample;

import com.whaley.core.repository.IRepository;

/**
 * Created by yangzhi on 2017/7/16.
 */

public class SampleMemoryRepository implements IRepository{

    @Override
    public <T> T obtainService(Class<T> aClass) {
        return null;
    }
}
