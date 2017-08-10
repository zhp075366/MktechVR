package com.gotech.vrplayer.module.localvideo.local;

import android.content.Context;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoPresenter implements ILocalVideoContract.Presenter {

    private Context mContext;
    private ILocalVideoContract.View mView;

    public LocalVideoPresenter(Context context, ILocalVideoContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void startPresenter() {

    }

    @Override
    public void destroyPresenter() {

    }
}
