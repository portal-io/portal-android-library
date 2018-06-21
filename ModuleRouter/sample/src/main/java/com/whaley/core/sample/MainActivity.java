package com.whaley.core.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.LogInterceptor;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Debug.buildLog()
                .tag("Sample")
                .methodOffset(2)
                .methodCount(2)
                .intercept(new LogInterceptor() {
                    @Override
                    public boolean log(int i, String s, String s1) {
                        return false;
                    }
                })
                .showThreadInfo(true)
                .build();

        Router.openDebug();
        Router.openLog();
        Router.init(getApplication());




        findViewById(R.id.btn_navigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance()
                        .buildNavigation("/sample/test1")
                        .withInt("key",1)
                        .navigation();
            }
        });

        findViewById(R.id.btn_executor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Router.getInstance()
//                        .buildExecutor("/sample/task/toast")
//                        .putObjParam(123)
//                        .callback(new Executor.Callback<String>() {
//                            @Override
//                            public void onCall(String data) {
//                                Toast.makeText(getApplicationContext(),"callback data ="+data,Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onFail(Executor.ExecutionError executeError) {
//                                Toast.makeText(getApplicationContext(),executeError.getMessage(),Toast.LENGTH_LONG).show();
//                            }
//                        })
//                        .excute();

                Service service=Router.getInstance().buildObj("/sample/service").getObj();
                service.say(getApplicationContext());
            }
        });

        findViewById(R.id.btn_findobj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=Router.getInstance().buildObj("/sample/ui/test").getObj();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container,fragment,"testui")
                        .commitNowAllowingStateLoss();
            }
        });

    }
}
