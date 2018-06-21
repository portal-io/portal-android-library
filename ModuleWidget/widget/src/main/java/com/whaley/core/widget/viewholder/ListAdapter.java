package com.whaley.core.widget.viewholder;

import java.util.List;

/**
 * Created by yangzhi on 16/5/25.
 */
public interface ListAdapter<T> {

    void setData(List<T> datas);

    void updates();
}
