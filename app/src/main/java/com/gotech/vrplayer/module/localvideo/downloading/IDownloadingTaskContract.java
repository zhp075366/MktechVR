package com.gotech.vrplayer.module.localvideo.downloading;

import com.gotech.vrplayer.base.IBasePresenter;
import com.gotech.vrplayer.base.IBaseView;
import com.lzy.okserver.download.DownloadTask;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IDownloadingTaskContract {

    interface View extends IBaseView {

        void showDownloadingTasks(List<DownloadTask> data);

    }

    interface Presenter extends IBasePresenter {

        void startAllTasks();

        void removeOneTask(String tag);

        void restoreDownloadingTasks();

        void unRegisterListener();

    }
}
