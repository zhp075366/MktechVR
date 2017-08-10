package com.gotech.vrplayer.module.home;

import com.gotech.vrplayer.base.IBaseView;
import com.gotech.vrplayer.model.bean.HomeMultipleItemBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: This specifies the contract between the view and the presenter.
 */
public interface IHomeView extends IBaseView {

    int getRecommendID();

    void showFirstLoadData(List<HomeMultipleItemBean> data);

}
