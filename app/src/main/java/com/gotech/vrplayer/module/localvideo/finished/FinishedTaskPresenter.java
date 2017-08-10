package com.gotech.vrplayer.module.localvideo.finished;

import android.content.Context;

import com.gotech.vrplayer.module.localvideo.DownloadVideoManager;
import com.lzy.okgo.model.Progress;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class FinishedTaskPresenter implements IFinishedTaskContract.Presenter {

    private Context mContext;
    private IFinishedTaskContract.View mView;
    private DownloadVideoManager mDownloadVideoManager;

    public FinishedTaskPresenter(Context context, IFinishedTaskContract.View view) {
        mView = view;
        mContext = context;
        mDownloadVideoManager = DownloadVideoManager.getInstance();
    }

    @Override
    public void startPresenter() {

    }

    @Override
    public void destroyPresenter() {

    }

    @Override
    public void getFinishedTasks() {
        // 同步调用
        List<Progress> finishedTask = mDownloadVideoManager.getFinishedProgress();
        mView.showFinishedTasks(finishedTask);
    }
}
