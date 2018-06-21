package com.whaley.core.widget.banner;

import android.widget.ImageView;

import com.whaley.core.widget.viewpager.ViewPager;

import java.util.List;

public class IndicatorPageChangeListener implements ViewPager.OnPageChangeListener {
    private List<ImageView> pointViews;

    int normalIndicatorResourceId;

    int selectedIndicatorResourceId;

    int lastSelectedIndex;

    BannerAdapter adapter;

    public IndicatorPageChangeListener(BannerAdapter adapter, List<ImageView> pointViews, int normalIndicatorResourceId, int selectedIndicatorResourceId) {
        this.adapter = adapter;
        this.pointViews = pointViews;
        this.normalIndicatorResourceId = normalIndicatorResourceId;
        this.selectedIndicatorResourceId = selectedIndicatorResourceId;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int index) {
        pointViews.get(lastSelectedIndex).setImageResource(normalIndicatorResourceId);
        pointViews.get(adapter.getRealPosition(index)).setImageResource(selectedIndicatorResourceId);
        lastSelectedIndex = adapter.getRealPosition(index);
    }

}