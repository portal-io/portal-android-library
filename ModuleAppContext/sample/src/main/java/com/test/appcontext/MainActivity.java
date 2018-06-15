package com.test.appcontext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.AppFileStorage;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_context).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContextProvider.getInstance().getContext() != null)
                    Toast.makeText(AppContextProvider.getInstance().getContext(), "获取到context", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.tv_appdir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContextProvider.getInstance().getContext() != null)
                    Toast.makeText(AppContextProvider.getInstance().getContext(), AppFileStorage.getAppDirPath(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
