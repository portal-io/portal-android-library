package com.whaley.core.widget.viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.widget.viewholder.AbsViewHolder;

import java.util.LinkedList;
import java.util.Queue;

/**
 * {@link PagerAdapter} implementation where each page is a regular view. Supports views recycling.
 * <p/>
 * Inspired by {@link RecyclerView.Adapter}.
 */
public abstract class RecyclerPagerAdapter<VH extends AbsViewHolder>
        extends PagerAdapter {

    private final Queue<VH> mCache = new LinkedList<>();
    private final SparseArray<VH> mAttached = new SparseArray<>();

    protected Object lock=new Object();

    public abstract VH onCreateViewHolder(@NonNull ViewGroup container, int viewType);

    public abstract void onBindViewHolder(@NonNull VH holder, int position);

    public void onRecycleViewHolder(@NonNull VH holder) {
    }

    protected int getItemType(int position){
        return 0;
    }

    protected boolean isRecycle(){
        return true;
    }

    /**
     * Returns ViewHolder for given position if it exists within ViewPager, or null otherwise
     */
    public VH getViewHolder(int position) {
        return mAttached.get(position);
    }

    public SparseArray<VH> getmAttached() {
        return mAttached;
    }

    public void onAttached(){

    }

    public void onDettached(){

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        synchronized (container) {
            position=getRealPosition(position);
            VH holder;
            int type = getItemType(position);
            if(isRecycle()) {
                holder = mCache.poll();
                if (holder == null || holder.getType() != type) {
                    holder = onCreateViewHolder(container, type);
                    holder.setType(type);
                }
            }else {
                holder = onCreateViewHolder(container, type);
                holder.setType(type);
            }
            holder.setPosition(position);
            if(isRecycle())
                mAttached.put(position, holder);

            // We should not use previous layout params, since ViewPager stores
            // important information there which cannot be reused
            container.addView(holder.getItemView(), holder.getItemView().getLayoutParams());

            onBindViewHolder(holder, position);
            return holder;
        }
    }

    public int getRealPosition(int position){
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        synchronized (container) {
            VH holder = (VH) object;
            if(isRecycle())
            mAttached.remove(position);
            container.removeView(holder.getItemView());
            if(isRecycle())
            mCache.offer(holder);
            onRecycleViewHolder(holder);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        AbsViewHolder holder = (AbsViewHolder) object;
        return holder.getItemView() == view;
    }

    @Override
    public int getItemPosition(Object object) {
        // Forcing all views reinitialization when data set changed.
        // It should be safe because we're using views recycling logic.
        return POSITION_NONE;
    }

}
