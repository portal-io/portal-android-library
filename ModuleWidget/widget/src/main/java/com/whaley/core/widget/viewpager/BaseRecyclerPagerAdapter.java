package com.whaley.core.widget.viewpager;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.core.widget.viewholder.AbsViewHolder;
import com.whaley.core.widget.viewholder.ListAdapter;
import com.whaley.core.widget.viewholder.OnItemClickListener;

import java.util.List;

/**
 * Created by YangZhi on 2016/9/4 2:39.
 */
public abstract class BaseRecyclerPagerAdapter<T,VH extends AbsViewHolder> extends RecyclerPagerAdapter<VH> implements ListAdapter<T> {

    protected List<T> datas;

    protected OnItemClickListener onItemClickListener;

    protected int count;
    @Override
    public void setData(List<T> datas) {
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

    @Override
    public void updates() {
        notifyDataSetChanged();
    }

    public T getItem(int position){
        if(datas!=null&&datas.size()>position){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);


    public abstract void onBindViewHolder(VH holder, T data,int position);


    @Override
    public final void onBindViewHolder(@NonNull final VH holder, final int position) {
        holder.setPosition(position);
        T data=getItem(position);
        holder.bindModel(data);
        onBindViewHolder(holder,data,position);
        setItemClick(holder,position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void setItemClick(final VH holder, final int position){
        if(onItemClickListener!=null)
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder,position);
                }
            });
    }


}
