package com.gotech.vrplayer.module.main;

import com.gotech.vrplayer.base.IBasePresenter;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IMainContract {

    interface View {

    }

    interface Presenter extends IBasePresenter {

        void checkUpdate();
    }
}
