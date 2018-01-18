package com.mktech.smarthome.module.video.detail;

import com.mktech.smarthome.base.IBaseView;
import com.mktech.smarthome.model.bean.HomePictureBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IVideoDetailView extends IBaseView {

    int getLoadPageNum();

    void showNetError();

    void showFirstLoadData(List<HomePictureBean> data);

    void showLoadMoreData(List<HomePictureBean> data);
}
