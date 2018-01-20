package com.mktech.smarthome.module.video.detail;

import android.content.Context;

import com.mktech.smarthome.event.LoadingDataEvent;
import com.mktech.smarthome.model.VideoDetailModel;
import com.mktech.smarthome.module.local.DownloadVideoManager;
import com.mktech.smarthome.utils.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoDetailPresenter {

    private String mEventCode;
    private Context mContext;
    private VideoDetailModel mModel;
    private IVideoDetailView mView;
    private DownloadVideoManager mDownloadVideoManager;

    public VideoDetailPresenter(Context context, IVideoDetailView view) {
        mView = view;
        mContext = context;
        mModel = new VideoDetailModel();
        mDownloadVideoManager = DownloadVideoManager.getInstance();
        mEventCode = LoadingDataEvent.VIDEO_DETAIL_EVENT_CODE;
        EventBus.getDefault().register(this);
    }

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

    public void loadMore() {
        boolean hasNetWork = NetworkUtil.checkNetworkConnection(mContext);
        if (!hasNetWork) {
            mView.showNetError();
            return;
        }
        int page = mView.getLoadPageNum();
        mModel.loadMore(page);
    }

    public void getFirstLoadData() {
        mView.showLoading();
        mModel.getFirstLoadData();
    }

    public AddTaskResult addTask() {
        return mDownloadVideoManager.addOneTask();
    }
}
