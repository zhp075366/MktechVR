package com.gotech.vrplayer.module.local.local;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.model.ILocalVideoModel;
import com.gotech.vrplayer.model.bean.LocalVideoBean;
import com.gotech.vrplayer.model.impl.LocalVideoModelImpl;
import com.gotech.vrplayer.utils.AsyncTaskWrapper;
import com.gotech.vrplayer.utils.DensityUtil;
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
    private AsyncTaskWrapper.OnLoadListener mLoadVideoListener;
    private AsyncTaskWrapper<Void, Void, List<LocalVideoBean>> mLoadVideoTask;
    // 视频缩略图加载任务
    private ThumbnailAsyncTask[] mThumbnailTasks;

    public LocalVideoPresenter(Context context, ILocalVideoView view) {
        mContext = context;
        mView = view;
        mModel = new LocalVideoModelImpl(mContext);
        initLoadVideoListener();
    }

    public void destroyPresenter() {
        if (mLoadVideoTask != null) {
            mLoadVideoTask.cancle();
        }
        if (mThumbnailTasks == null) {
            return;
        }
        for (ThumbnailAsyncTask task : mThumbnailTasks) {
            if (task != null) {
                task.cancle();
            }
        }
    }

    public void getAllVideo() {
        mLoadVideoTask = new AsyncTaskWrapper<>();
        mLoadVideoTask.setOnLoadListener(mLoadVideoListener);
        mLoadVideoTask.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_CACHED);
    }

    public void getThumbnail(int position, String videoPath) {
        if (mThumbnailTasks[position] != null) {
            return;
        }
        ThumbnailAsyncTask task = new ThumbnailAsyncTask(position, videoPath);
        mThumbnailTasks[position] = task;
        mThumbnailTasks[position].executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_EXECUTOR);
    }

    public void initThumbnailTask(int size) {
        mThumbnailTasks = new ThumbnailAsyncTask[size];
    }

    private void initLoadVideoListener() {
        mLoadVideoListener = new AsyncTaskWrapper.OnLoadListener<Void, Void, List<LocalVideoBean>>() {
            @Override
            public void onStart(Object taskTag) {
                mView.showLoading();
            }

            @Override
            public void onCancel(Object taskTag) {

            }

            @Override
            public void onResult(Object taskTag, List<LocalVideoBean> localVideoBeen) {
                KLog.i("loadVideo onResult");
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

    private class ThumbnailAsyncTask extends AsyncTaskWrapper<Void, Void, Bitmap> {

        private String mVideoPath;
        private int mWidth, mHeight;
        private Resources mResources = mContext.getResources();
        private AsyncTaskWrapper.OnLoadListener mThumbnailListener;

        public ThumbnailAsyncTask(int position, String videoPath) {
            initThumbnailListener();
            super.setTaskTag(position);
            super.setOnLoadListener(mThumbnailListener);
            mVideoPath = videoPath;
            mWidth = (int)DensityUtil.dp2Px(mContext, mResources.getDimension(R.dimen.local_video_thumbnail_width));
            mHeight = (int)DensityUtil.dp2Px(mContext, mResources.getDimension(R.dimen.local_video_thumbnail_height));
        }

        private void initThumbnailListener() {
            mThumbnailListener = new AsyncTaskWrapper.OnLoadListener<Void, Void, Bitmap>() {
                @Override
                public void onStart(Object taskTag) {
                    KLog.i("onStart->" + taskTag);
                }

                @Override
                public void onCancel(Object taskTag) {
                    KLog.i("onCancel->" + taskTag);
                }

                @Override
                public void onResult(Object taskTag, Bitmap bitmap) {
                    KLog.i("onResult->" + taskTag);
                    mView.setThumbnail((int)taskTag, bitmap);
                }

                @Override
                public void onProgress(Object taskTag, Void values) {

                }

                @Override
                public Bitmap onWorkerThread(Object taskTag, Void... params) {
                    return mModel.getVideoThumbnail(mVideoPath, mWidth, mHeight, MediaStore.Video.Thumbnails.MINI_KIND);
                }
            };
        }
    }
}
