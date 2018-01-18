package com.mktech.smarthome.module.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: ZouHaiping on 2017/7/18
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomeItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemPadding;
    private int mBottomPadding;

    public HomeItemDecoration(int itemPadding, int bottomPadding) {
        mItemPadding = itemPadding;
        mBottomPadding = bottomPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 此position包括header(header的position=0)
        // position=1以后与data list保持一致
        int position = parent.getChildAdapterPosition(view);
        if (position % 6 == 2) {
            // 右边和下边
            outRect.set(0, 0, mItemPadding, mBottomPadding);
        } else if (position % 6 == 3) {
            // 左边和下边
            outRect.set(mItemPadding, 0, 0, mBottomPadding);
        } else if (position % 6 == 4) {
            // 右边
            outRect.set(0, 0, mItemPadding, 0);
        } else if (position % 6 == 5) {
            // 左边
            outRect.set(mItemPadding, 0, 0, 0);
        }
    }
}
