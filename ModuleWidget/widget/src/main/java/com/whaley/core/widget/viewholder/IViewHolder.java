package com.whaley.core.widget.viewholder;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by YangZhi on 2017/7/24 12:29.
 */

public interface IViewHolder {

    int NO_POSITION = -1;

    View getItemView();

    void setPosition(int position);

    int getPosition();

    int getOldPosition();

    int getType();

    void setType(int type);

    <P extends View> P  getView(@IdRes int id);

    <P extends View> P  findView(View view,@IdRes int id);

    void putView(View view,@IdRes int id);

    <R> R getObj(String key);

    void putObj(String key,Object obj);

    void addImageView(ImageView imageView);

    void bindModel(Object model);

    <R> R getBindModel();
}
