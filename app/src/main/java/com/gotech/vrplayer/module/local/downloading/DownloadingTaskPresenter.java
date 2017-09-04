package com.gotech.vrplayer.module.local.downloading;

import android.content.Context;

import com.gotech.vrplayer.module.local.DownloadVideoManager;
import com.gotech.vrplayer.utils.AsyncTaskWrapper;
import com.lzy.okserver.download.DownloadTask;
import com.socks.library.KLog;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class DownloadingTaskPresenter {

    private Context mContext;
    private IDownloadingTaskView mView;
    private DownloadVideoManager mDownloadVideoManager;
    private AsyncTaskWrapper<Void, Void, List<DownloadTask>> mTask;
    private AsyncTaskWrapper.OnLoadListener mLoadDBListener;

    public DownloadingTaskPresenter(Context context, IDownloadingTaskView view) {
        mView = view;
        mContext = context;
        mDownloadVideoManager = DownloadVideoManager.getInstance();
        initLoadDBListener();
    }

    public void destroyPresenter() {
        if (mTask != null) {
            mTask.cancle();
        }
        unRegisterListener();
    }

    public void unRegisterListener() {
        // 取消下载进度监听
        mDownloadVideoManager.unRegisterListener();
    }

    public void removeOneTask(String tag) {
        // 在onFinish时从OkDownload中移除taskMap，以确保taskMap中为正在下载中的任务
        mDownloadVideoManager.removeOneTask(tag);
    }

    public void startAllTasks() {
        // 重启一次所有的Tasks，防止因异常中止的情况，让其继续下载
        mDownloadVideoManager.startAllTasks();
    }

    public void restoreDownloadingTasks() {
        // 改为异步调用
        mTask = new AsyncTaskWrapper<>();
        mTask.setOnTaskListener(mLoadDBListener);
        mTask.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_CACHED);
    }

    private void initLoadDBListener() {
        mLoadDBListener = new AsyncTaskWrapper.OnLoadListener<Void, Void, List<DownloadTask>>() {
            @Override
            public void onStart(Object taskTag) {
                mView.showLoading();
            }

            @Override
            public void onResult(Object taskTag, List<DownloadTask> tasks) {
                KLog.i("onResult");
                mView.hideLoading();
                mView.showDownloadingTasks(tasks);
            }

            @Override
            public List<DownloadTask> onWorkerThread(Object taskTag, Void... params) {
                return mDownloadVideoManager.restoreDownloadingTasks();
            }
        };
    }
}
