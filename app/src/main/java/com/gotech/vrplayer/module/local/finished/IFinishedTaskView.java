package com.gotech.vrplayer.module.local.finished;

import com.gotech.vrplayer.base.IBaseView;
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
