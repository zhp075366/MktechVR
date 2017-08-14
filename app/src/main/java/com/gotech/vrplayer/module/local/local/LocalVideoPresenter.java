package com.gotech.vrplayer.module.local.local;

import android.content.Context;

import com.gotech.vrplayer.model.ILocalVideoModel;
import com.gotech.vrplayer.model.bean.LocalVideoBean;
import com.gotech.vrplayer.model.impl.LocalVideoModelImpl;
import com.gotech.vrplayer.utils.ExAsyncTask;
import com.socks.library.KLog;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoPresenter {

    private Context mContext;
    private ILocalVideoView mView;
    private ILocalVideoModel mModel;
    private ExAsyncTask<Void, Void, List<LocalVideoBean>> mTask;
    private ExAsyncTask.OnLoadListener mLoadVideoListener;

    public LocalVideoPresenter(Context context, ILocalVideoView view) {
        mContext = context;
        mView = view;
        mModel = new LocalVideoModelImpl(mContext);
        initLoadVideoListener();
    }

    public void destroyPresenter() {
        if (mTask != null) {
            mTask.cancle();
        }
    }

    public void getAllVideo() {
        mTask = new ExAsyncTask<>();
        mTask.setOnLoadListener(mLoadVideoListener);
        mTask.executeOnExecutor(ExAsyncTask.THREAD_POOL_CACHED);
    }

    private void initLoadVideoListener() {
        mLoadVideoListener = new ExAsyncTask.OnLoadListener<Void, Void, List<LocalVideoBean>>() {
            @Override
            public void onStart(Object taskTag) {
                mView.showLoading();
            }

            @Override
            public void onCancel(Object taskTag) {

            }

            @Override
            public void onResult(Object taskTag, List<LocalVideoBean> localVideoBeen) {
                KLog.i("onResult");
                mView.hideLoading();
                mView.showLocalVideo(localVideoBeen);
            }

            @Override
            public void onProgress(Object taskTag, Void values) {

            }

            @Override
            public List<LocalVideoBean> onWorkerThread(Object taskTag, Void... params) {
                return mModel.getLocalVideoData();
            }
        };
    }
}
