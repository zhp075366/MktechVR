package com.gotech.vrplayer.module.localvideo.finished;

import com.gotech.vrplayer.base.IBasePresenter;
import com.gotech.vrplayer.base.IBaseView;
import com.lzy.okgo.model.Progress;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IFinishedTaskContract {

    interface View extends IBaseView {

        void showFinishedTasks(List<Progress> data);
    }

    interface Presenter extends IBasePresenter {

        void getFinishedTasks();

    }
}
