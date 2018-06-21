package com.whaley.core.widget.adapter;


import android.content.Context;
import android.content.MutableContextWrapper;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.widget.viewholder.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要加头部或尾部item的基类RecyclerView adapter
 * Created by yangzhi on 16/5/23.
 */
public class HeadFootAdapter extends
        Adapter<ViewHolder> implements ListAdapter {
    private static final int ITEM_TYPE_HEADER = -1000;
    private static final int ITEM_TYPE_FOOTER = 1000;

    private Context context;
    private Adapter adapter;

    private List<MoreViewHolder> headViews = new ArrayList<>();
    private List<MoreViewHolder> footViews = new ArrayList<>();

    private List<Integer> headTypes = new ArrayList<>();

    private List<Integer> footTypes = new ArrayList<>();


    private int count;

    private int adapterCount;

    public HeadFootAdapter(Context context,Adapter adapter) {
        this.context = context;
        setWrapAdapter(adapter);
        adapterCount=adapter.getItemCount();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if(!(holder instanceof MoreViewHolder)){
            adapter.onViewRecycled(holder);
        }else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.onViewRecycled();
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(!(holder instanceof MoreViewHolder)){
            adapter.onViewDetachedFromWindow(holder);
        }
    }

    public Adapter getAdapter() {
        return adapter;
    }

    @Override
    public void setData(List datas) {
        if (adapter instanceof ListAdapter) {
            ListAdapter listAdapter = (ListAdapter) adapter;
            listAdapter.setData(datas);
        }
    }

    @Override
    public void updates() {
        notifyDataSetChanged();
    }

    public void addHeader(View headView) {
        headViews.add(new MoreViewHolder(headView));
        updateCount();
    }

    private void updateCount(){
        count=adapter.getItemCount() + headViews.size()
                + footViews.size();
    }

    public void removeAllHeader() {
        headViews.removeAll(headViews);
        updateCount();
    }

    public void addFooter(View footView) {
        footViews.add(new MoreViewHolder(footView));
        updateCount();
    }

    public void removeAllFooter() {
        footViews.removeAll(footViews);
        updateCount();
    }

    private static class MoreViewHolder extends ViewHolder {
        View view;

        public MoreViewHolder(View arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
            this.view = arg0;
        }

        public void onViewRecycled(){
            Context context = view.getContext();
            if(context instanceof MutableContextWrapper){
                MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
                contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
            }
        }

        public void setRealContext(Context realContext){
            Context context = view.getContext();
            if(context instanceof MutableContextWrapper){
                MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
                contextWrapper.setBaseContext(realContext);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (position < headViews.size()) {
            int type = position + 10000;
            headTypes.add(type);
            return type;
        } else if (position < adapter.getItemCount() + headViews.size()) {
            if(adapter instanceof HeadFootAdapter){
//                Logger.i("nestAdapter type="+adapter.getItemViewType(position - headViews.size()));
            }
            return adapter.getItemViewType(position - headViews.size());
        } else {
            int type = position + 10000;
            footTypes.add(type);
//            Logger.i("type="+type);
            return type;
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return count;
    }

    public int getFooterSize() {
        return footViews.size();
    }

    public int getHeaderSize() {
        return headViews.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // TODO Auto-generated method stub
        if (!(viewHolder instanceof MoreViewHolder)) {
            adapter.onBindViewHolder(viewHolder, position - getHeaderSize());
        }else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) viewHolder;
            moreViewHolder.setRealContext(context);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        // TODO Auto-generated method stub
        this.context = viewGroup.getContext();
        ViewHolder viewHolder;
//        Logger.i("itemType="+itemType+",headersize="+getHeaderSize()+",footersize="+getFooterSize()+",adapter itemcount="+adapterCount);

        if (headTypes.contains(itemType)) {
            int position = itemType - 10000;
            viewHolder = headViews.get(position);
        } else if (footTypes.contains(itemType)) {
            int position = itemType - 10000;
            viewHolder = footViews.get(position - getHeaderSize() - adapterCount);
        } else {
            viewHolder = adapter.onCreateViewHolder(viewGroup, itemType);
        }

        return viewHolder;
    }

    private AdapterDataObserver observer = new AdapterDataObserver() {
        HeadFootAdapter headfootAdapter = HeadFootAdapter.this;

        @Override
        public void onChanged() {
            updateCount();
            adapterCount=adapter.getItemCount();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemRangeChanged(positionStart+headViews.size(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemRangeInserted(positionStart+headViews.size(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemRangeRemoved(positionStart+headViews.size(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition,
                                     int itemCount) {
            updateCount();
            adapterCount=adapter.getItemCount();
            headfootAdapter.notifyItemMoved(fromPosition+headViews.size(), itemCount);
        }
    };

    public void setWrapAdapter(Adapter adapter) {
        if (adapter == null)
            return;
        if (this.adapter != null)
            this.adapter.unregisterAdapterDataObserver(observer);
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(observer);

    }

}
