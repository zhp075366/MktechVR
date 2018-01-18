package com.mktech.smarthome.model;

import com.mktech.smarthome.model.bean.DownloadVideoBean;
import com.lzy.okgo.model.Progress;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/7/25
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IDownloadVideoModel {

    List<DownloadVideoBean> getNeedDownloadTasks();

    List<Progress> getFinishedProgress();

    List<Progress> getDownloadingProgress();

}
