package com.whaley.core.widget.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.whaley.core.widget.viewholder.IViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

    private int position = NO_POSITION;

    private int type;

    private final SparseArray<View> viewCache = new SparseArray<>();

    private final Map<String, Object> otherCache = new HashMap<>();

    private final List<ImageView> imageViews = new ArrayList<>();

    private Object bindModel;

    public ViewHolder(View view) {
        super(view);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View getItemView() {
        return itemView;
    }

    @Override
    public <P extends View> P getView(@IdRes int id) {
        View cacheView = viewCache.get(id);
        if (cacheView == null) {
            cacheView = findView(getItemView(), id);
            putView(cacheView, id);
        }
        return (P) cacheView;
    }

    @Override
    public <P extends View> P findView(View view, @IdRes int id) {
        View cacheView = view.findViewById(id);
        return (P) cacheView;
    }

    @Override
    public void putView(View view, @IdRes int id) {
        viewCache.put(id, view);
    }

    @Override
    public <R> R getObj(String key) {
        Object obj = otherCache.get(key);
        return (R) obj;
    }

    @Override
    public void putObj(String key, Object obj) {
        otherCache.put(key, obj);
    }


    /**
     * 添加所有使用 ImageLoader 加载图片的 ImageView
     * 为了过后统一当该 ViewHolder 被回收时释放图片资源
     *
     * @param imageView
     */
    @Override
    public void addImageView(ImageView imageView) {
        this.imageViews.add(imageView);
    }

    @Override
    public void bindModel(Object model) {
        this.bindModel = model;
    }

    @Override
    public <R> R getBindModel() {
        return (R)bindModel;
    }
}