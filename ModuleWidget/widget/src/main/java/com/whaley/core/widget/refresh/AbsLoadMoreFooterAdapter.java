package com.whaley.core.widget.refresh;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.widget.viewholder.ListAdapter;

import java.util.List;

/**
 * Created by YangZhi on 2017/9/25 21:34.
 */

public abstract class AbsLoadMoreFooterAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapter,LoadingPullableAdapter {
    public static final int ITEM_TYPE_LOADMORE = -1000;

    private Context context;
    private RecyclerView.Adapter adapter;


    private int count;

    private View.OnClickListener onClickListener;

    public AbsLoadMoreFooterAdapter(Context context, RecyclerView.Adapter adapter) {
        this.context = context;
        setWrapAdapter(adapter);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public final void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (!(holder instanceof MoreViewHolder)) {
            adapter.onViewRecycled(holder);
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            onFooterViewRecycled(moreViewHolder);
        }
    }

    @Override
    public final void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (!(holder instanceof MoreViewHolder)) {
            adapter.onViewDetachedFromWindow(holder);
        }else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            onFooterViewDetachedFromWindow(moreViewHolder);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (!(holder instanceof MoreViewHolder)) {
            adapter.onViewAttachedToWindow(holder);
        }else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            onFooterViewAttchedToWindow(moreViewHolder);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (!(holder instanceof MoreViewHolder)) {
            return adapter.onFailedToRecycleView(holder);
        }
        return super.onFailedToRecycleView(holder);
    }

    public void onFooterViewRecycled(MoreViewHolder moreViewHolder){
        moreViewHolder.onViewRecycled();
    }

    public void onFooterViewDetachedFromWindow(MoreViewHolder moreViewHolder){

    }

    public void onFooterViewAttchedToWindow(MoreViewHolder moreViewHolder){

    }

    public RecyclerView.Adapter getAdapter() {
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
        updateCount();
        notifyDataSetChanged();
    }

    private void updateCount() {
        count = adapter.getItemCount() > 0 && hasMore()? adapter.getItemCount() + 1 : adapter.getItemCount();
    }

    protected boolean hasMore(){
        return false;
    }

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
        View view;

        public MoreViewHolder(View arg0) {
            super(arg0);
            // TODO Auto-generated constructor stub
            this.view = arg0;
        }

        public void onViewRecycled() {
            Context context = view.getContext();
            if (context instanceof MutableContextWrapper) {
                MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
                contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
            }
        }

        public void setRealContext(Context realContext) {
            Context context = view.getContext();
            if (context instanceof MutableContextWrapper) {
                MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
                contextWrapper.setBaseContext(realContext);
            }
        }
    }

    @Override
    public final int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (position < adapter.getItemCount()) {
            return adapter.getItemViewType(position);
        } else {
            return ITEM_TYPE_LOADMORE;
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return count;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // TODO Auto-generated method stub
        if (!(viewHolder instanceof MoreViewHolder)) {
            adapter.onBindViewHolder(viewHolder, position);
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) viewHolder;
            moreViewHolder.setRealContext(context);
            onBindFooterViewHolder(moreViewHolder,position);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        // TODO Auto-generated method stub
        this.context = viewGroup.getContext();
        RecyclerView.ViewHolder viewHolder;
        if (itemType != ITEM_TYPE_LOADMORE) {
            viewHolder = adapter.onCreateViewHolder(viewGroup, itemType);
        } else {
            MoreViewHolder moreViewHolder = onCreateFooterViewHolder(viewGroup,itemType);
            viewHolder = moreViewHolder;
        }
        return viewHolder;
    }

    protected abstract void onBindFooterViewHolder(MoreViewHolder moreViewHolder,int position);

    protected abstract MoreViewHolder onCreateFooterViewHolder(ViewGroup viewGroup, int itemType);




    public void setWrapAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            return;
        if (this.adapter != null)
            this.adapter.unregisterAdapterDataObserver(observer);
        this.adapter = adapter;
        this.adapter.registerAdapterDataObserver(observer);
    }


    private AdapterObserver observer = new AdapterObserver(this);

    private static class AdapterObserver extends RecyclerView.AdapterDataObserver {
        private AbsLoadMoreFooterAdapter baseAdapter;

        public AdapterObserver(AbsLoadMoreFooterAdapter baseAdapter) {
            this.baseAdapter = baseAdapter;
        }

        @Override
        public void onChanged() {
            baseAdapter.updateCount();
            baseAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            baseAdapter.updateCount();
            baseAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            baseAdapter.updateCount();
            baseAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            baseAdapter.updateCount();
            baseAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition,
                                     int itemCount) {
            baseAdapter.updateCount();
            baseAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

}
