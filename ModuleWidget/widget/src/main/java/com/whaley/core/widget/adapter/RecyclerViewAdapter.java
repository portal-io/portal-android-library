package com.whaley.core.widget.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.MutableContextWrapper;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.List;

/**
 * Created by yangzhi on 2017/7/19.
 */

public abstract class RecyclerViewAdapter<T,VIEWHOLDER extends ViewHolder> extends RecyclerView.Adapter<VIEWHOLDER> implements ListAdapter<T> {


    private  String TAG;

    protected List<T> datas;

    private List<T> oldDatas;

    private OnItemClickListener onItemClickListener;

    protected int count;

    @Override
    public void setData(List<T> datas){
        if(TAG==null)
            TAG=getClass().getSimpleName();

        this.datas=datas;
        if(this.datas!=null)
            this.count=this.datas.size();
        else
            this.count=0;
        updates();
    }

    public List<T> getData(){
        return datas;
    }

    public List<T> getOldDatas() {
        return oldDatas;
    }

    /**
     * 根据数据刷新整个列表
     * 如果有老数据且有自定义的 DiffCallback 则做局部刷新
     * 否则直接 notifyDataSetChanged() 刷新完整列表
     */
    @Override
    public void updates() {
//        boolean isShouldNotify=true;
//        if(getOldDatas()!=null) {
//            DiffCallBack diffCallBack=getDiffCallback();
//            if(diffCallBack!=null) {
//                updateDiff(diffCallBack);
//                isShouldNotify=false;
//            }
//        }
//        if(isShouldNotify)
        notifyDataSetChanged();
//        this.oldDatas=this.datas;
    }

    protected DiffCallBack getDiffCallback(){
        return null;
    }

    /**
     * 根据DiffCallback做局部更新
     * @param diffCallBack
     */
    public void updateDiff(DiffCallBack diffCallBack){
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(diffCallBack, true);
        diffResult.dispatchUpdatesTo(this);
    }



    @Override
    public final void onBindViewHolder(final VIEWHOLDER holder, final int position) {
        holder.setPosition(position);
        T data=getItem(position);
        holder.bindModel(data);
        onBindViewHolder(holder,data,position);
        onSetItemClick(holder,position);

    }

    @Override
    public void onBindViewHolder(VIEWHOLDER holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }



    @Override
    public final VIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateNewViewHolder(parent,viewType);
    }

    public abstract VIEWHOLDER onCreateNewViewHolder(ViewGroup parent, int viewType);


    public abstract void onBindViewHolder(VIEWHOLDER holder, T data,int position);


    @Override
    public void onViewRecycled(VIEWHOLDER holder) {
        super.onViewRecycled(holder);
        Context context = holder.getItemView().getContext();
        if(context instanceof MutableContextWrapper){
            MutableContextWrapper contextWrapper = (MutableContextWrapper) context;
            contextWrapper.setBaseContext(AppContextProvider.getInstance().getContext());
        }
    }

    public T getItem(int position){
        if(datas!=null&&datas.size()>position){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return this.count;
    }


    public int getViewTypeCount() {
        return 1;
    }


    protected void onSetItemClick(final VIEWHOLDER holder, final int position){
        if(onItemClickListener!=null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder,position);
                }
            });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    public static class DiffCallBack<T> extends DiffUtil.Callback {
        private List<T> mOldDatas, mNewDatas;

        public DiffCallBack(List<T> mOldDatas, List<T> mNewDatas) {
            this.mOldDatas = mOldDatas;
            this.mNewDatas = mNewDatas;
        }

        @Override
        public int getOldListSize() {
            return mOldDatas != null ? mOldDatas.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewDatas != null ? mNewDatas.size() : 0;
        }

        public List<T> getmOldDatas() {
            return mOldDatas;
        }

        public List<T> getmNewDatas() {
            return mNewDatas;
        }

        /**
         * 这边根据你的需求去判断到底是刷新整个item，还是该item只有一点点数据的变化，从而去下面的方法判断是否需要刷新
         * 我的理解就是该item从旧数据到新数据我们用到的layout都变了，就是多布局，那就这边直接返回false，刷新整个item 也就是该 item 的 onCreateViewHolder 和 onBindViewHolder 将被调用
         * 如果返回true，讲继续执行下面的方法去判断。
         *
         * @param oldItemPosition
         * @param newItemPosition
         * @return
         */
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

            return false;
        }

        /**
         * 这里时判断新数据和老数据内容是否一致，如果一致则返回 true 代表不刷新item 如果返回 false ,将刷新 item 也就是该 item 的 onBindViewHolder 将被调用
         *
         * @param oldItemPosition
         * @param newItemPosition
         * @return
         */
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }
    }
}
