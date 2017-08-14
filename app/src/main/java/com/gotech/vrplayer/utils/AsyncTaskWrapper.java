package com.gotech.vrplayer.utils;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author: ZouHaiping on 2017/8/10
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 对AsyncTask进行的封装，如果你的任务的参数一样，可以只设置一个Listener，用mTaskTag来区分
 */
public class AsyncTaskWrapper<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Object mTaskTag;
    private OnLoadListener<Params, Progress, Result> mListener;
    public static final Executor THREAD_POOL_CACHED = Executors.newCachedThreadPool();
    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public void setTaskTag(Object taskTag) {
        mTaskTag = taskTag;
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mListener = listener;
    }

    public void cancle() {
        if (!isCancelled()) {
            cancel(true);
        }
    }

    public void updateProgress(Progress... values) {
        publishProgress(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onStart(mTaskTag);
    }

    @Override
    protected Result doInBackground(Params... params) {
        return mListener.onWorkerThread(mTaskTag, params);
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        mListener.onProgress(mTaskTag, values[0]);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        mListener.onResult(mTaskTag, result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListener.onCancel(mTaskTag);
    }

    public interface OnLoadListener<Params, Progress, Result> {

        void onStart(Object taskTag);

        void onCancel(Object taskTag);

        void onResult(Object taskTag, Result result);

        void onProgress(Object taskTag, Progress values);

        Result onWorkerThread(Object taskTag, Params... params);

    }
}
