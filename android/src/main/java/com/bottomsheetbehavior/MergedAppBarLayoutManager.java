package com.bottomsheetbehavior;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class MergedAppBarLayoutManager extends ViewGroupManager<AppBarLayout> {

    private final static String REACT_CLASS = "RCTMergedAppBarLayout";

    private String mMergedColor;
    private String mToolbarColor;
    private String mStatusBarColor;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    private MergedAppBarLayoutBehavior mergedBehavior;

    @Override
    public AppBarLayout createViewInstance(ThemedReactContext context) {
        AppBarLayout view = new AppBarLayout(context);
        int width = CoordinatorLayout.LayoutParams.MATCH_PARENT;
        int height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, height);
        params.setBehavior(new MergedAppBarLayoutBehavior(context, null));

        int statusBarHeight = getStatusBarHeight(context);
        params.setMargins(0, statusBarHeight, 0, 0);
        view.setLayoutParams(params);

        // Set tag to match on ScrollAwareFABBehavior.
        view.setTag("modal-appbar");
        return view;
    }

    @ReactProp(name = "mergedColor")
    public void setMergedColor(AppBarLayout view, String mergedColor) {
        mMergedColor = mergedColor;
    }

    @ReactProp(name = "toolbarColor")
    public void setToolbarColor(AppBarLayout view, String toolbarColor) {
        mToolbarColor = toolbarColor;
    }

    @ReactProp(name = "statusBarColor")
    public void setStatusBarColor(AppBarLayout view, String statusBarColor) {
        mStatusBarColor = statusBarColor;
    }

    @ReactProp(name = "height")
    public void setHeight(AppBarLayout view, int height) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) PixelUtil.toPixelFromDIP(height);
        view.setLayoutParams(params);
    }

    @Override
    public void addView(AppBarLayout parent, View child, int index) {
        if (child instanceof Toolbar) {
            int width = CoordinatorLayout.LayoutParams.MATCH_PARENT;
            int height = CoordinatorLayout.LayoutParams.MATCH_PARENT;

            CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, height);

            FrameLayout frame = new FrameLayout(parent.getContext());
            frame.setLayoutParams(params);

            // The backgroundView is used to merge the colors
            View backgroundView = new View((parent.getContext()));
            FrameLayout.LayoutParams backgroundParams = new FrameLayout.LayoutParams(width, 0);
            backgroundParams.gravity = Gravity.BOTTOM;
            backgroundView.setLayoutParams(backgroundParams);
            setMergedBackgroundColor(backgroundView);

            frame.addView(backgroundView);
            parent.addView(frame);
            Toolbar toolbar = (Toolbar) child;
            frame.addView(toolbar);
            mergedBehavior = MergedAppBarLayoutBehavior.from(parent);
            mergedBehavior.setToolbar(toolbar);
            mergedBehavior.setBackground(backgroundView);
            mergedBehavior.setToolbarTitle(toolbar.getTitle().toString());
            setFullBackGroundColor();
            setStatusBarColorBehavior();
        }
    }

    private void setMergedBackgroundColor(View view) {
        if (mMergedColor != null) {
            view.setBackgroundColor(Color.parseColor(mMergedColor));
        }
    }

    private void setFullBackGroundColor() {
        if (mToolbarColor != null) {
            mergedBehavior.setBackgroundColor(Color.parseColor(mToolbarColor));
        }
    }

    private void setStatusBarColorBehavior() {
        if (mStatusBarColor != null) {
            mergedBehavior.setStatusBarColor(Color.parseColor(mStatusBarColor));
        }
    }

    private int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
