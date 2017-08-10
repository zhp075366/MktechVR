package com.gotech.vrplayer.module.home;

import android.content.Context;

import com.gotech.vrplayer.event.HomeMessageEvent;
import com.gotech.vrplayer.model.IHomeModel;
import com.gotech.vrplayer.model.bean.HomeAdBean;
import com.gotech.vrplayer.model.bean.HomeCategoryBean;
import com.gotech.vrplayer.model.bean.HomeMultipleItemBean;
import com.gotech.vrplayer.model.impl.HomeModelImpl;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomePresenter implements IHomeContract.Presenter {

    private Context mContext;
    private IHomeModel mModel;
    private IHomeContract.View mView;

    public HomePresenter(Context context, IHomeContract.View view) {
        mView = view;
        mContext = context;
        mModel = new HomeModelImpl();
    }

    @Override
    public void startPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void destroyPresenter() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeMessageEvent(HomeMessageEvent<HomeMultipleItemBean> event) {
        HomeMessageEvent.EVENT_TYPE eventType = event.getEventType();
        if (eventType == HomeMessageEvent.EVENT_TYPE.TYPE_FIRST_LOAD_DATA) {
            mView.hideLoading();
            mView.showFirstLoadData(event.getData());
        }
    }

    // 异步请求网络数据
    @Override
    public void getFirstLoadData() {
        mView.showLoading();
        mModel.getFirstLoadData();
    }

    // 测试用的同步接口
    @Override
    public List<HomeCategoryBean> getCategoryData() {
        return mModel.getCategoryData();
    }

    // 测试用的同步接口
    @Override
    public List<HomeAdBean> getViewPagerData() {
        return mModel.getViewPagerData();
    }

    // 请求换一换的数据，同步接口，实际情况：第一次加载就已加载了所有后续需要换一换的数据
    @Override
    public List<HomeMultipleItemBean> getReplaceRecommendData() {
        // 根据RecommendID去获取对应的推荐数据
        int id = mView.getRecommendID();
        KLog.i("RecommendID:" + id);
        return mModel.getReplaceRecommendData();
    }
}
