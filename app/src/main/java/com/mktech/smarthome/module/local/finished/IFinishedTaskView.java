package com.mktech.smarthome.module.local.finished;

import com.mktech.smarthome.base.IBaseView;
import com.lzy.okgo.model.Progress;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IFinishedTaskView extends IBaseView {

    void showFinishedTasks(List<Progress> data);

}
