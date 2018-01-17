package com.gotech.vrplayer.utils;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Author: ZouHaiping on 2017/8/10
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 用于对GRPC Task进行的封装
 */
@SuppressWarnings({"unchecked"})
public class GRPCTaskWrapper<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private String mHost;
    private String mPort;
    private Object mTaskTag;
    private ManagedChannel mChannel;
    private OnLoadListener mListener;
    public static final Executor THREAD_POOL_CACHED = Executors.newCachedThreadPool();
    public static final Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public void setTaskTag(Object taskTag) {
        mTaskTag = taskTag;
    }

    public Object getTaskTag() {
        return mTaskTag;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public void setPort(String port) {
        mPort = port;
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
        mChannel = ManagedChannelBuilder.forAddress(mHost, Integer.valueOf(mPort)).usePlaintext(true).build();
        Result result = (Result)mListener.onWorkerThread(mTaskTag, mChannel, params);
        try {
            mChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return result;
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

        public void onCancel(Object taskTag) {
        }

        public void onProgress(Object taskTag, Progress... values) {
        }

        public abstract void onStart(Object taskTag);

        public abstract void onResult(Object taskTag, Result result);

        public abstract Result onWorkerThread(Object taskTag, ManagedChannel channel, Params... params);
    }
}
