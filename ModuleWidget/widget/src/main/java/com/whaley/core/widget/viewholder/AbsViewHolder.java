package com.whaley.core.widget.viewholder;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangZhi on 2017/7/24 12:35.
 */

public class AbsViewHolder implements IViewHolder{
    private int position = NO_POSITION;

    private int oldPosition = NO_POSITION;

    private int type;

    private final SparseArray<View> viewCache = new SparseArray<>();

    private final Map<String, Object> otherCache = new HashMap<>();

    private final List<ImageView> imageViews = new ArrayList<>();

    private View view;

    private Object bindModel;

    public AbsViewHolder(View view) {
        this.view = view;
    }

    @Override
    public View getItemView() {
        return view;
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
        this.oldPosition = this.position;
        this.position = position;
    }

    @Override
    public int getPosition() {
        return position;
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
    public int getOldPosition() {
        return oldPosition;
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
