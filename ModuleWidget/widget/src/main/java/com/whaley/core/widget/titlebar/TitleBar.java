package com.whaley.core.widget.titlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.StatusBarUtils;
import com.whaley.core.widget.R;

/**
 * Created by YangZhi on 2017/7/24 13:10.
 */

public class TitleBar extends FrameLayout implements ITitleBar {

    private static final int DEFAULT_CONTAINER_HEIGHT = DisplayUtil.convertDIP2PX(48);

    private static final int SIZE_ICON_PADDING = DisplayUtil.convertDIP2PX(20);

    private static final int NONE_RESID = -1;

    private final int STATUS_BAR_HEIGHT;

    private View leftView;

    private View rightView;

    private View centerView;

    private CharSequence titleText;

    private CharSequence rightText;

    private CharSequence leftText;

    private int rightIconResId = NONE_RESID;

    private int leftIconResId = R.drawable.ic_titlebar_back_selector;

    private boolean isPaddingStatus;

    private RelativeLayout container;

    private View bottomLine;

    private FrameLayout leftContainer;

    private FrameLayout rightContainer;

    private FrameLayout centerContainer;

    private int conatinerHeight = DEFAULT_CONTAINER_HEIGHT;

    private TitleBarListener listener;

    private boolean isShowBottomLine = true;

    int titleColor;
    int titleSize;
    int rightColor;
    int rightSize;
    int leftColor;
    int leftSize;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        STATUS_BAR_HEIGHT = StatusBarUtils.getStatusBarOffsetPx(context);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.TitleBarLayout);
            titleColor = a.getColor(R.styleable.TitleBarLayout_titleColor, -1);
            rightColor = a.getColor(R.styleable.TitleBarLayout_rightColor, -1);
            leftColor = a.getColor(R.styleable.TitleBarLayout_leftColor, -1);
            titleSize = (int) a.getDimension(R.styleable.TitleBarLayout_titleSize, -1);
            rightSize = (int) a.getDimension(R.styleable.TitleBarLayout_rightSize, -1);
            leftSize = (int) a.getDimension(R.styleable.TitleBarLayout_leftSize, -1);
        }
        ViewGroup.LayoutParams layoutParams
                = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        LayoutInflater.from(context).inflate(R.layout.layout_titlebar_container, this, true);
        container = (RelativeLayout) findViewById(R.id.titlebar_container);
        bottomLine = findViewById(R.id.titlebar_bottom_line);
        leftContainer = (FrameLayout) findViewById(R.id.titlebar_left);
        rightContainer = (FrameLayout) findViewById(R.id.titlebar_right);
        centerContainer = (FrameLayout) findViewById(R.id.titlebar_center);
        leftContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null)
                    return;
                listener.onLeftClick(v);
            }
        });
        rightContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null)
                    return;
                listener.onRightClick(v);
            }
        });
        centerContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null)
                    return;
                listener.onTitleClick(v);
            }
        });
        updateHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (rightView == null) {
            buildDefualtRightView();
        }
        if (centerView == null) {
            buildDefualtCenterView();
        }
        if (leftView == null) {
            buildDefualtLeftView();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void setTitleText(CharSequence titleText) {
        this.titleText = titleText;
        if (centerView == null) {
            buildDefualtCenterView();
            return;
        }
        if (centerView instanceof TextView) {
            ((TextView) centerView).setText(titleText);
        }
    }

    @Override
    public void setCenterView(View view) {
        this.centerView = view;
        FrameLayout.LayoutParams layoutParams
                = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        centerContainer.addView(centerView, layoutParams);
    }

    @Override
    public void setRightIcon(@DrawableRes int resId) {
        this.rightIconResId = resId;
        this.rightText = null;
        if (rightView == null)
            return;
        if (rightView instanceof ImageView) {
            ((ImageView) rightView).setImageResource(rightIconResId);
            return;
        }
        if (rightView instanceof TextView) {
            ImageView imageView = createImageView();
            imageView.setImageResource(rightIconResId);
            rightView = imageView;
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            rightContainer.removeAllViews();
            rightContainer.addView(rightView, layoutParams);
        }
    }

    @Override
    public void setRightText(CharSequence rightText) {
        this.rightText = rightText;
        this.rightIconResId = NONE_RESID;
        if (rightView == null)
            return;
        if (rightView instanceof TextView) {
            ((TextView) rightView).setText(rightText);
            return;
        }
        if (rightView instanceof ImageView) {
            TextView textView = createTextView();
            textView.setText(rightText);
            rightView = textView;
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            rightContainer.removeAllViews();
            rightContainer.addView(rightView, layoutParams);
        }
    }

    @Override
    public void setRightView(View view) {
        if (this.rightView != null) {
            rightContainer.removeView(this.rightView);
        }
        this.rightView = view;
        FrameLayout.LayoutParams layoutParams
                = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        rightContainer.addView(rightView, layoutParams);
    }

    @Override
    public void setLeftIcon(@DrawableRes int resId) {
        this.leftIconResId = resId;
        this.leftText = null;
        if (leftView == null)
            return;
        if (leftView instanceof ImageView) {
            ((ImageView) leftView).setImageResource(leftIconResId);
            return;
        }
        if (leftView instanceof TextView) {
            ImageView imageView = createImageView();
            imageView.setImageResource(leftIconResId);
            leftView = imageView;
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            leftContainer.removeAllViews();
            leftContainer.addView(leftView, layoutParams);
        }
    }

    @Override
    public void setLeftText(CharSequence leftText) {
        this.leftText = leftText;
        this.leftIconResId = NONE_RESID;
        if (leftView == null)
            return;
        if (leftView instanceof TextView) {
            ((TextView) leftView).setText(leftText);
            return;
        }
        if (leftView instanceof ImageView) {
            TextView textView = createTextView();
            textView.setText(leftText);
            leftView = textView;
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            rightContainer.removeAllViews();
            rightContainer.addView(leftView, layoutParams);
        }
    }

    @Override
    public void setLeftView(View view) {
        if (this.leftView != null) {
            leftContainer.removeView(this.leftView);
        }
        this.leftView = view;
        FrameLayout.LayoutParams layoutParams
                = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        leftContainer.addView(leftView, layoutParams);
    }

    @Override
    public void setPaddingStatus(boolean paddingStatus) {
        isPaddingStatus = paddingStatus;
        updateHeight();
    }

    private void buildDefualtRightView() {
        if (rightIconResId != NONE_RESID) {
            ImageView imageView = createImageView();
            imageView.setImageResource(rightIconResId);
            rightView = imageView;
        } else if (!TextUtils.isEmpty(rightText)) {
            TextView textView = createTextView();
            if (rightColor != -1) {
                textView.setTextColor(rightColor);
            }
            if (rightSize != -1) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightSize);
            }
            textView.setText(rightText);
            rightView = textView;
        }
        if (rightView == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams
                = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        rightContainer.addView(rightView, layoutParams);
    }

    private void buildDefualtCenterView() {
        if (!TextUtils.isEmpty(titleText)) {
            TextView textView = new TextView(getContext());
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSelected(true);
            if (titleColor != -1) {
                textView.setTextColor(titleColor);
            }
            if (titleSize != -1) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            }
            textView.setText(titleText);
            textView.setGravity(Gravity.CENTER);
            centerView = textView;
        }
        if (centerView == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams
                = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        centerContainer.addView(centerView, layoutParams);
    }

    private void buildDefualtLeftView() {
        if (leftIconResId != NONE_RESID) {
            ImageView imageView = createImageView();
            imageView.setImageResource(leftIconResId);
            leftView = imageView;
        } else if (!TextUtils.isEmpty(leftText)) {
            TextView textView = createTextView();
            if (leftColor != -1) {
                textView.setTextColor(leftColor);
            }
            if (leftSize != -1) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftSize);
            }
            textView.setText(leftText);
            leftView = textView;
        }
        if (leftView == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftContainer.addView(leftView, layoutParams);
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setPadding(SIZE_ICON_PADDING, 0, SIZE_ICON_PADDING, 0);
        return imageView;
    }

    private TextView createTextView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(SIZE_ICON_PADDING, 0, SIZE_ICON_PADDING, 0);
        return textView;
    }

    @Override
    public void setContainerHeight(int height) {
        this.conatinerHeight = height;
        updateHeight();
    }

    @Override
    public void setTitleBarListener(TitleBarListener listener) {
        this.listener = listener;
    }

    @Override
    public RelativeLayout getContainer() {
        return container;
    }

    @Override
    public View getLeftView() {
        return leftContainer;
    }

    @Override
    public View getRightView() {
        return rightContainer;
    }

    @Override
    public View getCenterView() {
        return centerContainer;
    }

    @Override
    public void setBottomLineColor(@ColorInt int color) {
        bottomLine.setBackgroundColor(color);
    }

    @Override
    public void setBottomLineResource(@ColorRes int resId) {
        setBottomLineColor(getResources().getColor(resId));
    }

    @Override
    public void hideBottomLine() {
        this.isShowBottomLine = false;
        bottomLine.setVisibility(GONE);
        updateContainerBottomMargin();
        updateHeight();
    }

    @Override
    public void showBottomLine() {
        this.isShowBottomLine = true;
        bottomLine.setVisibility(VISIBLE);
        updateContainerBottomMargin();
        updateHeight();
    }

    @Override
    public void setBottomLineHeight(int height) {
        bottomLine.getLayoutParams().height = height;
        updateContainerBottomMargin();
        updateHeight();
    }

    @Override
    public String getTitleText() {
        if (titleText != null)
            return titleText.toString();
        return null;
    }

    @Override
    public String getLeftText() {
        if (leftText != null)
            return leftText.toString();
        return null;
    }

    @Override
    public String getRightText() {
        if (rightText != null)
            return rightText.toString();
        return null;
    }

    @Override
    public void setRightViewVisibility(int visibility) {
        if (rightView != null) {
            if (rightView.getVisibility() != visibility) {
                rightView.setVisibility(visibility);
            }
        }
    }

    @Override
    public void setLeftViewVisibility(int visibility) {
        if (leftView != null) {
            if (leftView.getVisibility() != visibility) {
                leftView.setVisibility(visibility);
            }
        }
    }

    private void updateContainerBottomMargin() {
        ((MarginLayoutParams) container.getLayoutParams()).bottomMargin
                = isShowBottomLine ? bottomLine.getLayoutParams().height : 0;
    }

    private void updateHeight() {
        int height = isPaddingStatus ? STATUS_BAR_HEIGHT + conatinerHeight : conatinerHeight;
        if (isShowBottomLine) {
            height = height + bottomLine.getLayoutParams().height;
        }
        getLayoutParams().height = height;
        requestLayout();
    }
}
