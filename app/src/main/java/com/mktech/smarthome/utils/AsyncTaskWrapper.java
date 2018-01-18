package com.mktech.smarthome.utils;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author: ZouHaiping on 2017/8/10
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 对AsyncTask进行的封装
 * 如果你的任务参数一样，可以只设置一个Listener，用mTaskTag来区分
 */
@SuppressWarnings({"unchecked"})
public class AsyncTaskWrapper<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Object mTaskTag;
    private OnLoadListener mListener;
    public static final Executor THREAD_POOL_CACHED = Executors.newCachedThreadPool();
    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public void setTaskTag(Object taskTag) {
        mTaskTag = taskTag;
    }

    public Object getTaskTag() {
        return mTaskTag;
    }

    public void setOnTaskListener(OnLoadListener listener) {
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
        return (Result)mListener.onWorkerThread(mTaskTag, params);
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        mListener.onProgress(mTaskTag, values);
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

    public static abstract class OnLoadListener<Params, Progress, Result> {
        public void onStart(Object taskTag) {
        }

        public void onCancel(Object taskTag) {
        }

        public void onProgress(Object taskTag, Progress... values) {
        }

        public abstract void onResult(Object taskTag, Result result);

        public abstract Result onWorkerThread(Object taskTag, Params... params);
    }
}
