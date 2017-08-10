package com.gotech.vrplayer.model;

import com.gotech.vrplayer.model.bean.HomeAdBean;
import com.gotech.vrplayer.model.bean.HomeCategoryBean;
import com.gotech.vrplayer.model.bean.HomeMultipleItemBean;

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
