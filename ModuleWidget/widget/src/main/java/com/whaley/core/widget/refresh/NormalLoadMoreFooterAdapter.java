package com.whaley.core.widget.refresh;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.R;

/**
 * Created by yangzhi on 2017/9/26.
 */

public class NormalLoadMoreFooterAdapter extends AbsLoadMoreFooterAdapter {

    static final int DEFAULT_HEIGHT = DisplayUtil.convertDIP2PX(50);


    private LoadMoreData loadMoreData;



    public NormalLoadMoreFooterAdapter(Context context, RecyclerView.Adapter adapter) {
        super(context, adapter);
    }



    public void setData(LoadMoreData loadMoreData) {
        this.loadMoreData = loadMoreData;
    }

    public LoadMoreData getLoadMoreData() {
        return loadMoreData;
    }

    @Override
    protected boolean hasMore() {
        return loadMoreData == null ? false : loadMoreData.hasMore;
    }

    @Override
    protected AbsLoadMoreFooterAdapter.MoreViewHolder onCreateFooterViewHolder(ViewGroup viewGroup, int itemType) {
        MutableContextWrapper contextWrapper = new MutableContextWrapper(viewGroup.getContext());
        View view = LayoutInflater.from(contextWrapper).cloneInContext(contextWrapper).inflate(R.layout.layout_normal_loadmore, viewGroup, false);
        return new NormalFooterViewHolder(view);
    }

    @Override
    public void onFooterViewRecycled(MoreViewHolder moreViewHolder) {
        super.onFooterViewRecycled(moreViewHolder);
        NormalFooterViewHolder holder = (NormalFooterViewHolder) moreViewHolder;
        holder.itemView.setOnClickListener(null);
    }

    @Override
    protected void onBindFooterViewHolder(MoreViewHolder moreViewHolder, int position) {
        LoadMoreData loadMoreData = getLoadMoreData();
        NormalFooterViewHolder holder = (NormalFooterViewHolder) moreViewHolder;
        holder.itemView.setOnClickListener(null);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        int height = DEFAULT_HEIGHT;
        if (!loadMoreData.hasMore) {
            height = 0;
            holder.progressBar.setVisibility(View.GONE);
        }
        if (layoutParams.height != height) {
            holder.itemView.requestLayout();
            if (!loadMoreData.hasMore) {
                return;
            }
        }
        if (loadMoreData != holder.getLoadMoreData()) {
            switch (loadMoreData.state) {
                case LoadMoreData.STATE_INIT:
                    if (holder.itemView.getVisibility() == View.VISIBLE) {
                        holder.itemView.setVisibility(View.INVISIBLE);
                    }
                    holder.tvText.setText("");
                    holder.progressBar.setVisibility(View.GONE);
                    holder.itemView.setEnabled(false);
                    break;
                case LoadMoreData.STATE_SUCCESS:
                    if (holder.itemView.getVisibility() != View.VISIBLE) {
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
                    holder.itemView.setEnabled(false);
                    holder.tvText.setText(R.string.text_loadmore_success);
                    holder.progressBar.setVisibility(View.GONE);
                    break;
                case LoadMoreData.STATE_FAIL:
                    if (holder.itemView.getVisibility() != View.VISIBLE) {
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
                    holder.itemView.setEnabled(true);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.tvText.setText(R.string.text_loadmore_fail);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getOnClickListener()!=null){
                                getOnClickListener().onClick(v);
                            }
                        }
                    });
                    break;
                case LoadMoreData.STATE_LOADING:
                    if (holder.itemView.getVisibility() != View.VISIBLE) {
                        holder.itemView.setVisibility(View.VISIBLE);
                    }
                    holder.tvText.setText(R.string.text_loadmore_onloading);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.itemView.setEnabled(false);
                    break;
            }
        }
    }

    public static class NormalFooterViewHolder extends MoreViewHolder {

        TextView tvText;

        ProgressBar progressBar;

        View itemView;

        LoadMoreData loadMoreData;


        public NormalFooterViewHolder(View view) {
            super(view);
            itemView = view;
            tvText = (TextView) view.findViewById(R.id.tv_text);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_progress);
            view.setEnabled(false);
            view.setVisibility(View.INVISIBLE);
        }

        public LoadMoreData getLoadMoreData() {
            return loadMoreData;
        }
    }


    @Override
    public void onSuccess() {
        checkLoadMoreData();
        loadMoreData.state = LoadMoreData.STATE_SUCCESS;
        setData(loadMoreData);
        updates();
    }

    @Override
    public void onFailue() {
        checkLoadMoreData();
        loadMoreData.state = LoadMoreData.STATE_FAIL;
        setData(loadMoreData);
        updates();
    }

    @Override
    public void onLoadingData() {
        checkLoadMoreData();
        loadMoreData.state = LoadMoreData.STATE_LOADING;
        setData(loadMoreData);
        updates();
    }

    @Override
    public void setHasMoreData(boolean hasMore, boolean isRefresh) {
        checkLoadMoreData();
        loadMoreData.hasMore = hasMore;
        setData(loadMoreData);
        if(!isRefresh&&!hasMore) {
            Toast.makeText(AppContextProvider.getInstance().getContext(), R.string.text_loadmore_end, Toast.LENGTH_LONG).show();
        }
        updates();
    }

    private void checkLoadMoreData(){
        if(loadMoreData == null){
            loadMoreData = new LoadMoreData();
        }
    }

    public static class LoadMoreData {
        static final int STATE_INIT = 0;
        static final int STATE_SUCCESS = 1;
        static final int STATE_FAIL = 2;
        static final int STATE_LOADING = 3;

        private int state;

        private boolean hasMore;

        public void setState(int state) {
            this.state = state;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }
    }
}
