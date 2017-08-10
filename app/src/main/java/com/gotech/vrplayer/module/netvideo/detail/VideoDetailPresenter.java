package com.gotech.vrplayer.module.netvideo.detail;

import android.content.Context;

import com.gotech.vrplayer.event.LoadingDataEvent;
import com.gotech.vrplayer.model.IVideoDetailModel;
import com.gotech.vrplayer.model.impl.VideoDetailModelImpl;
import com.gotech.vrplayer.module.localvideo.DownloadVideoManager;
import com.gotech.vrplayer.utils.NetworkUtil;

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
    private IVideoDetailModel mModel;
    private IVideoDetailView mView;
    private DownloadVideoManager mDownloadVideoManager;

    public VideoDetailPresenter(Context context, IVideoDetailView view) {
        mView = view;
        mContext = context;
        mModel = new VideoDetailModelImpl();
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
