package com.gotech.vrplayer.module.local.local;

import com.gotech.vrplayer.base.IBaseView;
import com.gotech.vrplayer.model.bean.LocalVideoBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/10
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface ILocalVideoView extends IBaseView {

    void showLocalVideo(List<LocalVideoBean> data);

}
