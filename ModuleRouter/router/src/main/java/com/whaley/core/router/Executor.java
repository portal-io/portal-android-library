package com.whaley.core.router;

import com.alibaba.android.arouter.facade.template.IProvider;

import java.util.Map;

/**
 * Created by YangZhi on 2017/7/12 23:23.
 */

public interface Executor<T> extends IProvider{

    void excute(T params,Callback callback);

    interface Callback<T>{
        void onCall(T data);

        void onFail(ExecutionError executeError);
    }

    abstract class ExecutionError extends Error{
        public ExecutionError(String msg,Throwable cause){
            super("Execution error by "+msg,cause);
        }
    }


    class NotFoundExecutionError extends ExecutionError{
        public NotFoundExecutionError(String errorPath){
            super(errorPath+" this path is not registed!",null);
        }
    }


    class SourceExecutionError extends ExecutionError{
        public SourceExecutionError(String errorPath,Throwable cause){
            super(errorPath+" this Executor source is throwing exception!", cause);
        }
    }
}
