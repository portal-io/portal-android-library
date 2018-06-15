package com.whaleyvr.core.local.db;

import java.util.List;

/**
 * Created by dell on 2017/7/11.
 */

public class ListUtils {

    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

}
