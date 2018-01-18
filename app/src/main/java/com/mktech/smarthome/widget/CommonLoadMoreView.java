package com.mktech.smarthome.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.mktech.smarthome.R;

/**
 * Author: ZouHaiping on 2017/8/23
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public final class CommonLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.layout_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
