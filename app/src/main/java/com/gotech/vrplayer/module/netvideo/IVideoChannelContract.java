package com.gotech.vrplayer.module.netvideo;

import com.gotech.vrplayer.base.IBasePresenter;
import com.gotech.vrplayer.base.IBaseView;
import com.gotech.vrplayer.model.bean.HomePictureBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IVideoChannelContract {

    interface View extends IBaseView {

        int getLoadPageNum();

        String getChannelCode();

        void showNetError();
        
        void showFirstLoadData(List<HomePictureBean> data);

        void showLoadMoreData(List<HomePictureBean> data);
    }

    interface Presenter extends IBasePresenter {

        void loadMore();

        void getFirstLoadData();
    }
}
