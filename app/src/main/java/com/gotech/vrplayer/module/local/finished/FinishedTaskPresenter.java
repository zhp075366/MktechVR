package com.gotech.vrplayer.module.local.finished;

import android.content.Context;

import com.gotech.vrplayer.module.local.DownloadVideoManager;
import com.gotech.vrplayer.utils.AsyncTaskWrapper;
import com.lzy.okgo.model.Progress;
import com.socks.library.KLog;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class FinishedTaskPresenter {

    private Context mContext;
    private IFinishedTaskView mView;
    private DownloadVideoManager mDownloadVideoManager;
    private AsyncTaskWrapper<Void, Void, List<Progress>> mTask;
    private AsyncTaskWrapper.OnLoadListener mLoadDBListener;

    public FinishedTaskPresenter(Context context, IFinishedTaskView view) {
        mView = view;
        mContext = context;
        mDownloadVideoManager = DownloadVideoManager.getInstance();
        initLoadDBListener();
    }

    public void destroyPresenter() {
        if (mTask != null) {
            mTask.cancle();
        }
    }

    public void getFinishedTasks() {
        // 改为异步调用
        mTask = new AsyncTaskWrapper<>();
        mTask.setOnTaskListener(mLoadDBListener);
        mTask.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_CACHED);
    }

    private void initLoadDBListener() {
        mLoadDBListener = new AsyncTaskWrapper.OnLoadListener<Void, Void, List<Progress>>() {
            @Override
            public void onStart(Object taskTag) {
                mView.showLoading();
            }

            @Override
            public void onCancel(Object taskTag) {

            }

            @Override
            public void onResult(Object taskTag, List<Progress> progresses) {
                KLog.i("onResult");
                mView.hideLoading();
                mView.showFinishedTasks(progresses);
            }

            @Override
            public void onProgress(Object taskTag, Void... values) {

            }

            @Override
            public List<Progress> onWorkerThread(Object taskTag, Void... params) {
                return mDownloadVideoManager.getFinishedProgress();
            }
        };
    }
}
