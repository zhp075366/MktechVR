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
    // 加载视频缩略图任务
    private AsyncTaskWrapper[] mThumbnailTasks;
    private AsyncTaskWrapper.OnLoadListener<String, Void, Bitmap> mLoadThumbnailListener;
    // 加载所有视频任务
    private AsyncTaskWrapper<Void, Void, List<LocalVideoBean>> mLoadVideoTask;
    private AsyncTaskWrapper.OnLoadListener<Void, Void, List<LocalVideoBean>> mLoadVideoListener;

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
        for (AsyncTaskWrapper task : mThumbnailTasks) {
            if (task != null) {
                task.cancle();
            }
        }
    }

    public void getAllVideo() {
        initLoadVideoListener();
        mLoadVideoTask = new AsyncTaskWrapper<>();
        mLoadVideoTask.setOnTaskListener(mLoadVideoListener);
        mLoadVideoTask.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_CACHED);
    }

    public void getThumbnail(int position, String videoPath) {
        if (mThumbnailTasks[position] != null) {
            return;
        }
        AsyncTaskWrapper<String, Void, Bitmap> task = new AsyncTaskWrapper<>();
        task.setTaskTag(position);
        task.setOnTaskListener(mLoadThumbnailListener);
        task.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_EXECUTOR, videoPath);
        mThumbnailTasks[position] = task;
    }

    public void initLoadThumbnailTask(int size) {
        initLoadThumbnailListener();
        mThumbnailTasks = new AsyncTaskWrapper[size];
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

    private void initLoadThumbnailListener() {
        mLoadThumbnailListener = new AsyncTaskWrapper.OnLoadListener<String, Void, Bitmap>() {
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
    }
}
