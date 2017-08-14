package com.gotech.vrplayer.module.local;

import android.widget.Toast;

import com.gotech.vrplayer.VRApplication;
import com.gotech.vrplayer.model.IDownloadVideoModel;
import com.gotech.vrplayer.model.bean.DownloadVideoBean;
import com.gotech.vrplayer.model.impl.DownloadVideoModelImpl;
import com.gotech.vrplayer.module.video.detail.AddTaskResult;
import com.gotech.vrplayer.utils.SDCardUtil;
import com.gotech.vrplayer.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Author: ZouHaiping on 2017/7/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 任务的管理，增加，移除，查询等
 */
public class DownloadVideoManager {

    private OkDownload mOkDownload;
    private IDownloadVideoModel mModel;
    private List<DownloadVideoBean> mNeedDownList;

    // 静态内部类单例模式
    private static final class SingletonHolder {
        private static final DownloadVideoManager sInstance = new DownloadVideoManager();
    }

    public static DownloadVideoManager getInstance() {
        return SingletonHolder.sInstance;
    }

    private DownloadVideoManager() {
        initOkDownload();
        mModel = new DownloadVideoModelImpl();
        mNeedDownList = mModel.getNeedDownloadTasks();
    }

    private void initOkDownload() {
        //OkDownload 全局的下载管理类
        mOkDownload = OkDownload.getInstance();
        mOkDownload.setFolder(SDCardUtil.getDownloadSaveRootPath());
        KLog.i("OkDownload getFolder->" + mOkDownload.getFolder());
        mOkDownload.getThreadPool().setCorePoolSize(2);
    }

    // 测试用的添加任务方式
    public AddTaskResult addOneTask() {
        for (DownloadVideoBean bean : mNeedDownList) {
            AddTaskResult result = addTask(bean);
            if (result == AddTaskResult.ADD_OK) {
                return result;
            }
        }
        ToastUtil.showToast(VRApplication.getApplication(), "没有任务可添加", Toast.LENGTH_SHORT);
        return AddTaskResult.ADD_ERROR;
    }

    private AddTaskResult addTask(final DownloadVideoBean bean) {
        // 判断是否下载完成
        List<Progress> finishedList = getFinishedProgress();
        for (Progress progress : finishedList) {
            if (progress.tag.equals(bean.downUrl)) {
                KLog.i("ADD_FAILED_IS_FINISHED -> " + bean.showName);
                return AddTaskResult.ADD_FAILED_IS_FINISHED;
            }
        }
        // 判断是否正在下载
        List<Progress> downloadingList = getDownloadingProgress();
        for (Progress progress : downloadingList) {
            if (progress.tag.equals(bean.downUrl)) {
                KLog.i("ADD_FAILED_IS_DOWNLOADING -> " + bean.showName);
                return AddTaskResult.ADD_FAILED_IS_DOWNLOADING;
            }
        }
        // 可以开始添加
        String tag = bean.downUrl;
        // 构建一个下载请求Request
        GetRequest<File> request = OkGo.get(tag);
        // 构建下载任务，传入一个tag和我们上一步创建的request对象
        DownloadTask downloadTask = OkDownload.request(tag, request);
        downloadTask.extra1(bean).fileName(bean.saveName).totalSize(bean.totalSize).save().start();
        KLog.i("AddTaskResult.ADD_OK -> " + bean.showName);
        ToastUtil.showToast(VRApplication.getApplication(), "AddTaskResult.ADD_OK -> " + bean.showName, Toast.LENGTH_SHORT);
        return AddTaskResult.ADD_OK;
    }

    /**
     * 首次初始化OkDownload中的taskMap，我把它初始化为下载中的任务
     * 原因1：方便调用startAll()，只需start未完成的任务
     * 原因2：方便调用unRegister()，取消监听也只是对未完成的任务
     * <p>
     * 注意：但随着Downloading任务一个一个的Finish，taskMap中依然存在着这些任务，没有移除，作者设计taskMap为所有任务
     * 所以我在onFinish作移除，原因还是startAll()，只需start未完成的任务，作者对Progress.FINISH作了很多判断，个人觉得显得冗余
     */
    public List<DownloadTask> restoreDownloadingTasks() {
        List<Progress> progressList = getDownloadingProgress();
        return OkDownload.restore(progressList);
    }

    // 从mModel拿到正在下载中的任务
    private List<Progress> getDownloadingProgress() {
        return mModel.getDownloadingProgress();
    }

    // 从mModel中拿到已完成的任务
    public List<Progress> getFinishedProgress() {
        return mModel.getFinishedProgress();
    }

    // 取消下载中任务的进度监听
    public void unRegisterListener() {
        Map<String, DownloadTask> taskMap = mOkDownload.getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(task.progress.tag);
        }
    }

    public void startAllTasks() {
        // OkDownload中的taskMap已经初始化为未完成的任务，所以直接startAll
        mOkDownload.startAll();
    }

    public void removeOneTask(String tag) {
        mOkDownload.getTask(tag).unRegister(tag);
        mOkDownload.removeTask(tag);
    }
}
