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
    private Resources mResources;
    private ILocalVideoView mView;
    private ILocalVideoModel mModel;
    private int mWidth, mHeight;
    // 加载所有视频任务
    private AsyncTaskWrapper<Void, Void, List<LocalVideoBean>> mLoadVideoTask;
    // 加载视频缩略图任务
    private ThumbnailAsyncTask[] mThumbnailTasks;

    public LocalVideoPresenter(Context context, ILocalVideoView view) {
        mContext = context;
        mResources = mContext.getResources();
        mView = view;
        mModel = new LocalVideoModelImpl(mContext);
        mWidth = (int)DensityUtil.dp2Px(mContext, mResources.getDimension(R.dimen.local_video_thumbnail_width));
        mHeight = (int)DensityUtil.dp2Px(mContext, mResources.getDimension(R.dimen.local_video_thumbnail_height));
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
        ThumbnailAsyncTask task = new ThumbnailAsyncTask(position);
        mThumbnailTasks[position] = task;
        mThumbnailTasks[position].executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_EXECUTOR, videoPath);
    }

    public void initThumbnailTask(int size) {
        mThumbnailTasks = new ThumbnailAsyncTask[size];
    }


    private AsyncTaskWrapper.OnLoadListener mLoadVideoListener = new AsyncTaskWrapper.OnLoadListener<Void, Void, List<LocalVideoBean>>() {
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

    private AsyncTaskWrapper.OnLoadListener mThumbnailListener = new AsyncTaskWrapper.OnLoadListener<String, Void, Bitmap>() {
        @Override
        public void onStart(Object taskTag) {
            KLog.i("Thumbnail onStart->" + taskTag);
        }

        @Override
        public void onCancel(Object taskTag) {
            KLog.i("Thumbnail onCancel->" + taskTag);
        }

        @Override
        public void onResult(Object taskTag, Bitmap bitmap) {
            KLog.i("Thumbnail onResult->" + taskTag);
            mView.setThumbnail((int)taskTag, bitmap);
        }

        @Override
        public void onProgress(Object taskTag, Void values) {

        }

        @Override
        public Bitmap onWorkerThread(Object taskTag, String... params) {
            return mModel.getVideoThumbnail(params[0], mWidth, mHeight, MediaStore.Video.Thumbnails.MINI_KIND);
        }
    };

    private class ThumbnailAsyncTask extends AsyncTaskWrapper<String, Void, Bitmap> {

        public ThumbnailAsyncTask(int position) {
            super.setTaskTag(position);
            super.setOnLoadListener(mThumbnailListener);
        }

    }
}
