package com.mktech.smarthome.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: ZouHaiping on 2017/7/14
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 通知的RecyclerView ItemDecoration
 */
public class CommonLineDivider extends RecyclerView.ItemDecoration {

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mDividerHeight;
    private ColorDrawable mColorDrawable;
    private boolean mDrawLastItem = true;

    public CommonLineDivider(int color, int height, int paddingLeft, int paddingRight) {
        mDividerHeight = height;
        mColorDrawable = new ColorDrawable(color);
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
    }

    public void setDrawLastItem(boolean drawLastItem) {
        mDrawLastItem = drawLastItem;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = mPaddingLeft;
        int right = parent.getWidth() - mPaddingRight;
        // parent是我们能看到的部分
        int screenCount = parent.getChildCount();
        RecyclerView.Adapter adapter = parent.getAdapter();
        int itemCount = adapter.getItemCount();
        int lastPosition = itemCount - 1;

        for (int i = 0; i < screenCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            if (position == lastPosition && !mDrawLastItem) {
                continue;
            }

            // 获得childView的布局信息
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)childView.getLayoutParams();
            // childView底部以上的距离
            int itemBottom = childView.getBottom();
            // 设置childView根布局layout_marginBottom时有效
            int bottomMargin = params.bottomMargin;
            int top = itemBottom + bottomMargin;
            int bottom = top + mDividerHeight;
            // 设置绘制边界范围
            mColorDrawable.setBounds(left, top, right, bottom);
            // 绘制到canvas上
            mColorDrawable.draw(c);
        }
    }

    // 由于Divider也有长宽高，每一个Item需要向下偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int itemCount = adapter.getItemCount();
        int lastPosition = itemCount - 1;
        int position = parent.getChildAdapterPosition(view);
        if (position == lastPosition && !mDrawLastItem) {
            return;
        }
        // 画横线，就是往下偏移一个分割线的高度
        outRect.set(0, 0, 0, mDividerHeight);
    }
}