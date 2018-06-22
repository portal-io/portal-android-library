package com.whaley.module.player;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String[] ITEMS=new String[]{
            "普通播放器"
            ,"直播播放器"
            ,"Banner播放器"
            ,"带数据解析的播放器"
            ,"带付费的播放器"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView textView=new TextView(parent.getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.BLACK);
                RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
                textView.setLayoutParams(layoutParams);
                return new RecyclerView.ViewHolder(textView) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView=(TextView) holder.itemView;
                textView.setTag(position);
                textView.setText(ITEMS[position]);
                textView.setOnClickListener(MainActivity.this);
            }

            @Override
            public int getItemCount() {
                return ITEMS.length;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int position=(int)view.getTag();
        switch (position){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }
}
