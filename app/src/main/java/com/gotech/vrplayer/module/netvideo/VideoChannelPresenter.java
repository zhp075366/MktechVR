package com.gotech.vrplayer.module.netvideo;

import android.content.Context;

import com.gotech.vrplayer.event.LoadingDataEvent;
import com.gotech.vrplayer.model.IVideoChannelModel;
import com.gotech.vrplayer.model.impl.VideoChannelModelImpl;
import com.gotech.vrplayer.utils.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoChannelPresenter implements IVideoChannelContract.Presenter {

    private Context mContext;
    private IVideoChannelModel mModel;
    private IVideoChannelContract.View mView;
    private String mEventCode;

    public VideoChannelPresenter(Context context, IVideoChannelContract.View view) {
        mView = view;
        mContext = context;
        mModel = new VideoChannelModelImpl();
    }

    @Override
    public void startPresenter() {
        mEventCode = mView.getChannelCode();
        EventBus.getDefault().register(this);
    }

    @Override
    public void destroyPresenter() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadingDataEvent(LoadingDataEvent event) {
        if (!event.getEventCode().equals(mEventCode)) {
            return;
        }
        LoadingDataEvent.EVENT_TYPE eventType = event.getEventType();
        if (eventType == LoadingDataEvent.EVENT_TYPE.TYPE_FIRST_LOAD_DATA) {
            mView.hideLoading();
            mView.showFirstLoadData(event.getData());
        } else if (eventType == LoadingDataEvent.EVENT_TYPE.TYPE_LOAD_MORE_DATA) {
            mView.showLoadMoreData(event.getData());
        }
    }

    // 异步加载
    @Override
    public void loadMore() {
        boolean hasNetWork = NetworkUtil.checkNetworkConnection(mContext);
        if (!hasNetWork) {
            mView.showNetError();
            return;
        }
        int page = mView.getLoadPageNum();
        mModel.loadMore(page, mEventCode);
    }

    // 异步加载
    @Override
    public void getFirstLoadData() {
        mView.showLoading();
        mModel.getFirstLoadData(mEventCode);
    }
}
