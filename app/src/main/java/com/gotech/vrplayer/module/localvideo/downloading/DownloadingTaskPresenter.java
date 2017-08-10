package com.gotech.vrplayer.module.localvideo.downloading;

import android.content.Context;

import com.gotech.vrplayer.module.localvideo.DownloadVideoManager;
import com.lzy.okserver.download.DownloadTask;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class DownloadingTaskPresenter implements IDownloadingTaskContract.Presenter {

    private Context mContext;
    private IDownloadingTaskContract.View mView;
    private DownloadVideoManager mDownloadVideoManager;

    public DownloadingTaskPresenter(Context context, IDownloadingTaskContract.View view) {
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
    public void unRegisterListener() {
        // 取消下载进度监听
        mDownloadVideoManager.unRegisterListener();
    }

    @Override
    public void removeOneTask(String tag) {
        // 在onFinish时从OkDownload中移除taskMap，以确保taskMap中为正在下载中的任务
        mDownloadVideoManager.removeOneTask(tag);
    }

    @Override
    public void startAllTasks() {
        // 重启一次所有的Tasks，防止因异常中止的情况，让其继续下载
        mDownloadVideoManager.startAllTasks();
    }

    @Override
    public void restoreDownloadingTasks() {
        // 同步调用
        List<DownloadTask> downloadTasks = mDownloadVideoManager.restoreDownloadingTasks();
        mView.showDownloadingTasks(downloadTasks);
    }
}
