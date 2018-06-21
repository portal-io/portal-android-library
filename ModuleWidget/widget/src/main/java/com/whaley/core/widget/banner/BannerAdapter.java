package com.whaley.core.widget.banner;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.widget.viewholder.AbsViewHolder;
import com.whaley.core.widget.viewpager.RecyclerPagerAdapter;
import com.whaley.core.widget.viewpager.ViewPager;

import java.util.List;

/**
 * Created by yangzhi on 2017/9/9.
 */

public abstract class BannerAdapter<Model, VH extends BannerViewHolder> extends RecyclerPagerAdapter<VH> {

    private int realCount = 0;

    private VH currentViewHolder;

    private boolean isLoop;

    private List<Model> modelList;

    private BannerListener bannerListener;

    public BannerAdapter(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public void setBannerListener(BannerListener bannerListener) {
        this.bannerListener = bannerListener;
    }

    public void setData(List<Model> models) {
        this.modelList = models;
        this.realCount = getRealCount();
        this.isLoop = this.isLoop&&realCount>1;
        notifyDataSetChanged();
    }

    public List<Model> getDatas() {
        return modelList;
    }

    public int getRealCount() {
        return modelList == null ? 0 : modelList.size();
    }

    @Override
    public int getCount() {
        if (isLoop) {
            return realCount + 2;
        }
        return realCount;
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public int getRealPosition(int position) {
        if (!isLoop)
            return position;
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;
        return realPosition;
    }

    public int getInnerPosition(int position) {
        return isLoop ? position + 1 : position;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        VH viewHolder = (VH) object;
        if (viewHolder != currentViewHolder) {
            if (currentViewHolder != null) {
                onHideViewHolder(currentViewHolder);
            }
            currentViewHolder = viewHolder;
            onShowViewHolder(currentViewHolder);
            if (bannerListener != null) {
                bannerListener.onBannerSelected(currentViewHolder, currentViewHolder.getPosition());
            }
        }
    }

    protected void onHideViewHolder(VH viewHolder) {

    }

    protected void onShowViewHolder(VH viewHolder) {

    }

    protected void setItemClick(final VH holder) {
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerListener != null) {
                    bannerListener.onBannerItemClick(holder, holder.getPosition());
                }
            }
        });
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        Model model = getDatas().get(position);
        holder.bindModel(model);
        onBindViewHolder(holder, model, position);
        setItemClick(holder);
    }

    protected abstract void onBindViewHolder(@NonNull VH holder, Model model, int position);


    public interface BannerListener {
        void onBannerSelected(BannerViewHolder viewHolder, int position);

        void onBannerItemClick(BannerViewHolder viewHolder, int position);
    }
}
