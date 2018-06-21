package com.whaley.core.router;

import android.net.Uri;

import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.GsonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangzhi on 2017/7/13.
 */

public class ExecutorRouterMeta extends RouterMeta {

    private Map<String, Object> params;

    private Object param;

    private Executor.Callback executeCallback;

    private boolean isTransParam = true;

    private boolean isTransCalbackData = true;

    public ExecutorRouterMeta(String path) {
        super(path);
    }

    public ExecutorRouterMeta(String path, String group) {
        super(path, group);
    }

    public ExecutorRouterMeta(Uri url) {
        super(url);
    }

    public ExecutorRouterMeta putObjParam(Object param){
        this.params = null;
        this.param = param;
        return this;
    }

    public ExecutorRouterMeta putParams(Map<String, Object> params){
        this.param = null;
        this.params = params;
        return this;
    }

    public ExecutorRouterMeta putParam(String key, String param){
        if(params == null)
            params = new HashMap<>();
        this.param = null;
        params.put(key,param);
        return this;
    }

    public ExecutorRouterMeta callback(Executor.Callback executeCallback){
        this.executeCallback = executeCallback;
        return this;
    }

    public ExecutorRouterMeta notTransParam(){
        this.isTransParam = false;
        return this;
    }

    public ExecutorRouterMeta notTransCallbackData(){
        this.isTransCalbackData = false;
        return this;
    }


    public void excute() {
        Object object = getPostcard().navigation();
        if (object != null && object instanceof Executor) {
            Executor executor = (Executor) object;
            try {
                Object excuteParam = null;
                if(this.param != null){
                    excuteParam = isTransParam ? transformObjToObj(this.param,getType(executor)) : this.param;
                }else if(this.params != null){
                    excuteParam = isTransParam ? transformMapToObj(this.params, getType(executor)) : this.params;
                }
                executor.excute(excuteParam, new Executor.Callback() {
                    @Override
                    public void onCall(Object data) {
                        Object callbackObj = isTransCalbackData ? transformObjToObj(data,getType(executeCallback)) : data;
                        executeCallback.onCall(callbackObj);
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executeError) {
                        callbackError(executeError);
                    }
                });
            }catch (Exception e){
                Log.e(e,"ExcutorRouterMeta excute");
                callbackError(new Executor.SourceExecutionError(getPostcard().getPath()+" "+executor.getClass().getName(),e));
            }
            return;
        }
        callbackError(new Executor.NotFoundExecutionError(getPostcard().getPath()));
    }


    private void callbackError(Executor.ExecutionError executionError){
        if (executeCallback != null) {
            executeCallback.onFail(executionError);
        }
    }


    private Object transformObjToObj(Object object,Type type){
        String json = GsonUtil.getGson().toJson(object);
        return GsonUtil.getGson().fromJson(json,type);
    }

    private Object transformMapToObj(Map<String,Object> map,Type type){
        String json = GsonUtil.getGson().toJson(map);
        return GsonUtil.getGson().fromJson(json,type);
    }


    private Type getType(Object obj){
        Class clazz = obj.getClass();

        Type[] types = clazz.getGenericInterfaces();

        // 为了确保安全转换，使用instanceof
        if (types[0] instanceof ParameterizedType) {
            // 执行强制类型转换
            ParameterizedType parameterizedType = (ParameterizedType)types[0];
            // 获取基本类型信息，即Map
            Type basicType = parameterizedType.getRawType();
            Log.i("基本类型为："+basicType);
            // 获取泛型类型的泛型参数
            Type[] types2 = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < types2.length; i++) {
                Log.i("第"+(i+1)+"个泛型类型是："+types2[i]);
            }
            return types2[0];
        } else {
            Log.i("获取泛型类型出错!");
        }
        return null;
    }
}
