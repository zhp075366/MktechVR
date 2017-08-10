package com.gotech.vrplayer.module.main;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class MainPresenter {

    private MainActivity mActivity;
    private IMainView mView;

    public MainPresenter(MainActivity activity, IMainView view) {
        mView = view;
        mActivity = activity;
    }

    public void checkUpdate() {

    }
}
