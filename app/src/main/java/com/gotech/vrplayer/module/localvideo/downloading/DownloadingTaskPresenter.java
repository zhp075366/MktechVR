package com.gotech.vrplayer.module.localvideo.downloading;

import android.content.Context;

import com.gotech.vrplayer.module.localvideo.DownloadVideoManager;
import com.gotech.vrplayer.utils.ExAsyncTask;
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
    private ExAsyncTask<Void, Void, List<DownloadTask>> mTask;
    private static final int LOAD_DOWNLOADING_TASK_TAG = 1;
    private ExAsyncTask.OnLoadListener mLoadDBListener;

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
        mTask = new ExAsyncTask<>();
        mTask.setTaskTag(LOAD_DOWNLOADING_TASK_TAG);
        mTask.setOnLoadListener(mLoadDBListener);
        mTask.executeOnExecutor(ExAsyncTask.CACHE_EXECUTOR);
    }

    private void initLoadDBListener() {
        mLoadDBListener = new ExAsyncTask.OnLoadListener<Void, Void, List<DownloadTask>>() {
            @Override
            public void onStart(int taskTag) {
                mView.showLoading();
            }

            @Override
            public void onCancel(int taskTag) {

            }

            @Override
            public void onResult(int taskTag, List<DownloadTask> tasks) {
                KLog.i("onResult");
                mView.hideLoading();
                mView.showDownloadingTasks(tasks);
            }

            @Override
            public void onProgress(int taskTag, Void values) {

            }

            @Override
            public List<DownloadTask> onWorkerThread(int taskTag, Void... params) {
                return mDownloadVideoManager.restoreDownloadingTasks();
            }
        };
    }
}
