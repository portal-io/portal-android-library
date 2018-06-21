//package com.whaley.core.widget.refresh;
//
//import android.content.MutableContextWrapper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.whaley.core.utils.DisplayUtil;
//import com.whaley.core.widget.R;
//
///**
// * Created by yangzhi on 15/11/30.
// */
//public class NormalLoadMoreAdapter implements LoadingPullableAdapter {
//    static final int STATE_INIT = 0;
//    static final int STATE_SUCCESS = 1;
//    static final int STATE_FAIL = 2;
//    static final int STATE_LOADING = 3;
//
//    TextView tvText;
//
//    ProgressBar progressBar;
//
//    View view;
//
//    private View.OnClickListener onClickListener;
//
//    boolean isHasMore;
//
//    boolean isOnRefreshToSetHasMore;
//
//    int state;
//
//
//
//    public NormalLoadMoreAdapter() {
//        this(null);
//    }
//
//    public NormalLoadMoreAdapter(View.OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }
//
//
//    @Override
//    public View getView(ViewGroup parent) {
//        MutableContextWrapper contextWrapper = new MutableContextWrapper(parent.getContext());
//        view = LayoutInflater.from(contextWrapper).cloneInContext(contextWrapper).inflate(R.layout.layout_normal_loadmore, parent, false);
//        tvText = (TextView) view.findViewById(R.id.tv_text);
//        progressBar = (ProgressBar) view.findViewById(R.id.pb_progress);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onClickListener == null)
//                    return;
//                onClickListener.onClick(v);
//            }
//        });
//        view.setEnabled(false);
//        view.setVisibility(View.INVISIBLE);
//        setHasMoreData(isHasMore, isOnRefreshToSetHasMore);
//        return view;
//    }
//
//    public void setHasMoreData(boolean hasMore, boolean isRefresh) {
//        this.isHasMore = hasMore;
//        this.isOnRefreshToSetHasMore = isRefresh;
//        if (view == null)
//            return;
//        if (view.getVisibility() != View.VISIBLE) {
//            view.setVisibility(View.VISIBLE);
//        }
//        if (!hasMore) {
//            view.setEnabled(false);
//            ViewGroup.LayoutParams lp = view.getLayoutParams();
//            lp.height = 0;
//            view.setLayoutParams(lp);
//            if (!isRefresh) {
//                Toast.makeText(view.getContext(), R.string.text_loadmore_end, Toast.LENGTH_SHORT).show();
//            }
//            tvText.setVisibility(View.GONE);
//        } else {
//            tvText.setVisibility(View.VISIBLE);
//            view.setEnabled(true);
//            ViewGroup.LayoutParams lp = view.getLayoutParams();
//            lp.height = DisplayUtil.convertDIP2PX(50);
//            view.setLayoutParams(lp);
//            tvText.setText("");
//        }
//        progressBar.setVisibility(View.GONE);
//
//    }
////    @Override
////    public void setHasMoreData(boolean hasMore) {
//////        tvText.setVisibility(hasMore?View.VISIBLE:View.INVISIBLE);
////
////    }
//
//    @Override
//    public void onSuccess() {
//        state = STATE_SUCCESS;
//        if (view == null) {
//            return;
//        }
//        if (view.getVisibility() != View.VISIBLE) {
//            view.setVisibility(View.VISIBLE);
//        }
//        view.setEnabled(false);
//        tvText.setText(R.string.text_loadmore_success);
//        progressBar.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onFailue() {
//        state = STATE_FAIL;
//        if(view == null){
//            return;
//        }
//        if (view.getVisibility() != View.VISIBLE) {
//            view.setVisibility(View.VISIBLE);
//        }
//        view.setEnabled(true);
//        progressBar.setVisibility(View.GONE);
//        tvText.setText(R.string.text_loadmore_fail);
//    }
//
//    @Override
//    public void onLoadingData() {
//        state = STATE_LOADING;
//        if(view == null){
//            return;
//        }
//        if (view.getVisibility() != View.VISIBLE) {
//            view.setVisibility(View.VISIBLE);
//        }
//        tvText.setText(R.string.text_loadmore_onloading);
//        progressBar.setVisibility(View.VISIBLE);
//        tvText.setEnabled(false);
//    }
//
//    @Override
//    public void setOnClickListener(View.OnClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }
//}
