package com.gotech.vrplayer.module.localvideo.local;

import android.content.Context;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoPresenter {

    private Context mContext;
    private ILocalVideoView mView;

    public LocalVideoPresenter(Context context, ILocalVideoView view) {
        mContext = context;
        mView = view;
    }
}
