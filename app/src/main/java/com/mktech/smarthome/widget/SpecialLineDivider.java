package com.mktech.smarthome.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Author: ZouHaiping on 2017/7/14
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 此类是配合BaseRecyclerViewHelper来使用的
 */
public class SpecialLineDivider extends RecyclerView.ItemDecoration {

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mDividerHeight;
    private ColorDrawable mColorDrawable;
    private boolean mDrawLastItem = true;

    public SpecialLineDivider(int color, int height, int paddingLeft, int paddingRight) {
        mDividerHeight = height;
        mColorDrawable = new ColorDrawable(color);
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
    }

    public void setDrawLastItem(boolean drawLastItem) {
        mDrawLastItem = drawLastItem;
    }

    // 由于有分页加载，所以最后一个item position需要动态判断
    private int getLastItemPosition(BaseQuickAdapter adapter, int headerItem) {
        // 最后一个item position
        int lastItemPosition;
        // item总数
        int itemCount = adapter.getData().size();

        if (headerItem == 1) {
            lastItemPosition = itemCount;
        } else {
            lastItemPosition = itemCount - 1;
        }

        return lastItemPosition;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = mPaddingLeft;
        int right = parent.getWidth() - mPaddingRight;

        // parent是我们能看到的部分
        int screenCount = parent.getChildCount();
        BaseQuickAdapter adapter = (BaseQuickAdapter)parent.getAdapter();

        // 是否有HeaderView
        int headerItem = adapter.getHeaderLayoutCount();

        // 是否有loadMoreView
        int loadMoreViewItem = adapter.getLoadMoreViewCount();
        int loadMoreViewPosition = adapter.getLoadMoreViewPosition();

        for (int i = 0; i < screenCount; i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);

            if (loadMoreViewItem == 1 && (position == loadMoreViewPosition)) {
                continue;
            }
            if (headerItem == 1 && (position == 0)) {
                continue;
            }

            int lastItemPosition = getLastItemPosition(adapter, headerItem);
            if (position == lastItemPosition && !mDrawLastItem) {
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
        // 如果有header,此position包括header(header的position=0),position=1以后与data list保持一致
        int position = parent.getChildAdapterPosition(view);
        BaseQuickAdapter adapter = (BaseQuickAdapter)parent.getAdapter();

        // 是否有HeaderView
        int headerItem = adapter.getHeaderLayoutCount();

        // 是否有loadMoreView
        int loadMoreViewItem = adapter.getLoadMoreViewCount();
        int loadMoreViewPosition = adapter.getLoadMoreViewPosition();

        // loadMoreView不需要分割线
        if (loadMoreViewItem == 1 && (position == loadMoreViewPosition)) {
            return;
        }
        // HeaderView不需要分割线
        if (headerItem == 1 && (position == 0)) {
            return;
        }

        // 最后一个data item不需要分割线
        int lastItemPosition = getLastItemPosition(adapter, headerItem);
        if (position == lastItemPosition && !mDrawLastItem) {
            return;
        }

        // 画横线，就是往下偏移一个分割线的高度
        outRect.set(0, 0, 0, mDividerHeight);
    }
}