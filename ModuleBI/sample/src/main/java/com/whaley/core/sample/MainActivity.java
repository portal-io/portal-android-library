package com.whaley.core.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.whaley.core.bi.BILogServiceManager;
import com.whaley.core.bi.model.LogInfoParam;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGeneralBuilder();
            }
        });
    }


    private void getGeneralBuilder() {
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId("browse_view")
                .setCurrentPageId("topic")
                .putCurrentPagePropKeyValue("pageId", "1111111")
                .putCurrentPagePropKeyValue("pageName", "猜猜我是谁")
                .setNextPageId("topic");
        BILogServiceManager.getInstance().recordLog(builder);
    }
}
