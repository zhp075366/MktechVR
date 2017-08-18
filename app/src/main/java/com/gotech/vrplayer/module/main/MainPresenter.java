package com.gotech.vrplayer.module.main;

import android.content.Context;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class MainPresenter {

    private Context mContext;
    private IMainView mView;

    public MainPresenter(Context context, IMainView view) {
        mView = view;
        mContext = context;
    }

    public void destroyPresenter() {

    }
}
