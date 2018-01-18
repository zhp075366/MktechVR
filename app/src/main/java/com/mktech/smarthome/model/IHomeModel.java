package com.mktech.smarthome.model;

import com.mktech.smarthome.model.bean.HomeAdBean;
import com.mktech.smarthome.model.bean.HomeCategoryBean;
import com.mktech.smarthome.model.bean.HomeMultipleItemBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/22
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IHomeModel {

    void getFirstLoadData();

    List<HomeCategoryBean> getCategoryData();

    List<HomeAdBean> getViewPagerData();

    List<HomeMultipleItemBean> getReplaceRecommendData();
}
