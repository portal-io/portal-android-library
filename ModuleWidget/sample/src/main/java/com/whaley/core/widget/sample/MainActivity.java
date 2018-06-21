package com.whaley.core.widget.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.whaley.core.appcontext.AppContextInit;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.adapter.RecyclerViewAdapter;
import com.whaley.core.widget.adapter.ViewHolder;
import com.whaley.core.widget.banner.BannerAdapter;
import com.whaley.core.widget.banner.BannerViewHolder;
import com.whaley.core.widget.banner.WhaleyBanner;
import com.whaley.core.widget.refresh.OnLoadMoreListener;
import com.whaley.core.widget.refresh.RefreshLayout;
import com.whaley.core.widget.titlebar.ITitleBar;
import com.whaley.core.widget.titlebar.TitleBarListener;
import com.whaley.core.widget.viewholder.AbsViewHolder;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaley.core.widget.viewholder.OnItemClickListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int TYPE_BANNER = 1;

    ITitleBar titleBar;

    RefreshLayout refreshLayout;

    RecyclerView recyclerView;

    int loadMoreCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        StatusBarUtil.setTransparentFullStatusBar(getWindow(), new SystemBarTintManager(this), false);
        super.onCreate(savedInstanceState);
        AppContextInit.appContextInit(getApplicationContext(), "");
        setContentView(R.layout.activity_main);
        titleBar = (ITitleBar) findViewById(R.id.titleBar);
//        titleBar.setPaddingStatus(true);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//        titleBar.setTitleText("Demo首頁");
        titleBar.setRightText("注册");
        titleBar.setLeftText("取消");
        titleBar.hideBottomLine();
        titleBar.setTitleBarListener(new TitleBarListener() {
            @Override
            public void onLeftClick(View view) {
                titleBar.setRightText("天涯");
            }

            @Override
            public void onTitleClick(View view) {
                titleBar.setRightIcon(R.mipmap.ic_titlebar_back_black);
                titleBar.showBottomLine();
            }

            @Override
            public void onRightClick(View view) {
                titleBar.setRightIcon(R.mipmap.ic_titlebar_back_blue);
                titleBar.setTitleText("Demo首頁aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                titleBar.hideBottomLine();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter<String, ViewHolder>() {


            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return TYPE_BANNER;
                }
                return super.getItemViewType(position);
            }

            @Override
            public ViewHolder onCreateNewViewHolder(ViewGroup parent, int viewType) {
                if (viewType == TYPE_BANNER) {
                    WhaleyBanner banner = new WhaleyBanner(parent.getContext());
                    banner.setNormalIndicator(R.mipmap.page_indicator_unfocused)
                            .setIndicatorGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                            .setSelectedIndicator(R.mipmap.page_indicator_focused)
                            .setAutoChange(true)
                            .setAutoChangeDuration(3000)
//                            .setIndicatorEnable(false)
                            .setBannerListener(new BannerAdapter.BannerListener() {
                                @Override
                                public void onBannerSelected(BannerViewHolder viewHolder, int position) {

                                }

                                @Override
                                public void onBannerItemClick(BannerViewHolder viewHolder, int position) {

                                }
                            })
                            .setAdapter(new BannerAdapter<Integer, BannerViewHolder>(true) {
                                @Override
                                protected void onBindViewHolder(@NonNull BannerViewHolder holder, Integer resId, int position) {
                                    ImageView imageView = (ImageView) holder.getItemView();
                                    imageView.setImageResource(resId);
                                }

                                @Override
                                public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
                                    ImageView imageView = new ImageView(container.getContext());
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                    return new BannerViewHolder(imageView) {
                                    };
                                }
                            });
                    RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.convertDIP2PX(240));
                    banner.setLayoutParams(layoutParams);
                    return new ViewHolder(banner);
                }
                TextView textView = new TextView(parent.getContext());
                textView.setGravity(Gravity.CENTER);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.convertDIP2PX(150));
                textView.setLayoutParams(layoutParams);
                return new ViewHolder(textView);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, String data, int position) {
                if (position == 0) {
                    WhaleyBanner banner = (WhaleyBanner) holder.getItemView();
                    List<Integer> images = new ArrayList<>();
                    images.add(R.mipmap.bg_1);
//                    images.add(R.mipmap.bg_2);
//                    images.add(R.mipmap.bg_3);
//                    images.add(R.mipmap.bg_4);
//                    images.add(R.mipmap.bg_5);
//                    images.add(R.mipmap.bg_6);
                    banner.setData(images);
                    return;
                }
                TextView itemView = (TextView) holder.getItemView();
                itemView.setText(data);
            }
        };
        refreshLayout.setAdapter(recyclerView,adapter,false);
        refreshLayout.getLoadMoreView().setListener(new OnLoadMoreListener() {


            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> list = adapter.getData();
                        int index = loadMoreCount*10;
                        for (int i=0;i<10;i++){
                            String indexStr = "loadMore_"+(index+i+1);
                            list.add(indexStr);
                        }
                        adapter.setData(list);
                        refreshLayout.getLoadMoreView().stopLoadMore(true);
                        if(loadMoreCount<3){
                            refreshLayout.getLoadMoreView().setHasMore(true,false);
                            loadMoreCount++;
                            return;
                        }
                        refreshLayout.getLoadMoreView().setHasMore(false,false);
                    }
                }, 2000);

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData(adapter);
                        refreshLayout.stopRefresh(true);
                    }
                }, 2000);
            }
        });
//        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(IViewHolder viewHolder, int position) {
                startActivity(new Intent(MainActivity.this, FrameAnimActivity.class));
            }
        });
        refreshData(adapter);

    }


    private void refreshData(RecyclerViewAdapter adapter){
        List list = new ArrayList();
        for (int i=0;i<10;i++){
            String indexStr = "refrsh_"+(i+1);
            list.add(indexStr);
        }
        adapter.setData(list);
        loadMoreCount = 0;
        refreshLayout.getLoadMoreView().setHasMore(true,true);
    }
}
