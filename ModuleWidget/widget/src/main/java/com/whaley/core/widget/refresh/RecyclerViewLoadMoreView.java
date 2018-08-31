package com.whaley.core.widget.refresh;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yangzhi on 16/5/19.
 */
public class RecyclerViewLoadMoreView extends LoadMoreView<RecyclerView.OnScrollListener> {

    private RecyclerView.OnScrollListener onScrollListener;
    private boolean isAddScrollListener = true;

    public RecyclerViewLoadMoreView(final RecyclerView recyclerView, final RecyclerView.Adapter adapter, final boolean isBottomBar) {
        this(recyclerView, new NormalLoadMoreFooterAdapter(recyclerView.getContext(), adapter), isBottomBar, true);
    }

    public RecyclerViewLoadMoreView(final RecyclerView recyclerView, final RecyclerView.Adapter adapter, final boolean isBottomBar, boolean isAddScrollListener) {
        this(recyclerView, new NormalLoadMoreFooterAdapter(recyclerView.getContext(), adapter), isBottomBar, isAddScrollListener);
    }

    public RecyclerViewLoadMoreView(RecyclerView recyclerView, AbsLoadMoreFooterAdapter loadMoreFooterAdapter, boolean hasBlankBottom, boolean isAddScrollListener) {
        super(loadMoreFooterAdapter);
        this.isAddScrollListener = isAddScrollListener;
        checkSpanSize(recyclerView, hasBlankBottom);
        recyclerView.setAdapter(loadMoreFooterAdapter);
        setUpOnScrollListener(recyclerView);
        loadMoreFooterAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadMore();
            }
        });
//        getLoadingPullableAdapter().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startLoadMore();
//            }
//        });
    }


    protected void checkSpanSize(final RecyclerView recyclerView, boolean isBottomBar) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            final int lastPosition = isBottomBar ? 2 : 1;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHasMore() && position == recyclerView.getAdapter().getItemCount() - lastPosition) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    else
                        return 1;
                }
            });
        }
    }

    protected void setUpOnScrollListener(RecyclerView recyclerView) {
        if (isAddScrollListener) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (getOnScrollListener() != null)
                        getOnScrollListener().onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (getOnScrollListener() != null)
                        getOnScrollListener().onScrolled(recyclerView, dx, dy);
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (isHasMore()
                            && adapter.getItemCount() > 0
                            && layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - 1
                            && !isLoading()) {
                        startLoadMore();
                    }
                }
            });
        }
    }

    @Override
    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }
}
