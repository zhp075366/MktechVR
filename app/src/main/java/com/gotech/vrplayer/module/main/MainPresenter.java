package com.gotech.vrplayer.module.main;

import com.socks.library.KLog;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class MainPresenter implements IMainContract.Presenter {

    private MainActivity mActivity;
    private IMainContract.View mView;

    public MainPresenter(MainActivity activity, IMainContract.View view) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void startPresenter() {

    }

    @Override
    public void destroyPresenter() {

    }

    @Override
    public void checkUpdate() {

    }
}
