package com.gotech.vrplayer.module.localvideo.downloading;

import com.gotech.vrplayer.base.IBaseView;
import com.lzy.okserver.download.DownloadTask;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/10
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IDownloadingTaskView extends IBaseView {

    void showDownloadingTasks(List<DownloadTask> data);

}
