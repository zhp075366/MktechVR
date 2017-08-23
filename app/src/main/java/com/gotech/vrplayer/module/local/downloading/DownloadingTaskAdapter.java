package com.gotech.vrplayer.module.local.downloading;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.model.bean.DownloadVideoBean;
import com.gotech.vrplayer.utils.Constants;
import com.gotech.vrplayer.widget.NumberProgressBar;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: ZouHaiping on 2017/7/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class DownloadingTaskAdapter extends RecyclerView.Adapter<DownloadingTaskAdapter.ViewHolder> {

    private Context mContext;
    private List<DownloadTask> mData;
    private LayoutInflater mInflater;
    private DownloadingTaskPresenter mPresenter;

    public DownloadingTaskAdapter(Context context, DownloadingTaskPresenter presenter) {
        mContext = context;
        mPresenter = presenter;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void updateData() {
        mPresenter.restoreDownloadingTasks();
    }

    public void setData(List<DownloadTask> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_downloading_task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadTask task = mData.get(position);
        Progress progress = task.progress;
        String tag = progress.tag;
        task.register(new DownloadingTaskListener(tag, holder));
        holder.setTag(tag);
        holder.setTask(task);
        holder.bindItem(progress);
        holder.refresh(progress);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView mIcon;
        @BindView(R.id.show_name)
        TextView mShowName;
        @BindView(R.id.downloadSize)
        TextView mDownloadSize;
        @BindView(R.id.netSpeed)
        TextView mNetSpeed;
        @BindView(R.id.nbProgress)
        NumberProgressBar mNbProgress;

        private String tag;
        private DownloadTask task;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }

        public void bindItem(Progress progress) {
            DownloadVideoBean video = (DownloadVideoBean)progress.extra1;
            Glide.with(mContext).load(video.iconUrl).placeholder(R.mipmap.ic_launcher).into(mIcon);
            mShowName.setText(video.showName);
        }

        public void restart(Progress progress) {
            task.restart();
        }

        public void refresh(Progress progress) {
            // 设计成只有-> (等待中，包括网络错误)和(下载完成)
            String currentSize = Formatter.formatFileSize(mContext, progress.currentSize);
            String totalSize = Formatter.formatFileSize(mContext, progress.totalSize);
            mDownloadSize.setText(currentSize + "/" + totalSize);
            switch (progress.status) {
                case Progress.NONE:
                    break;
                case Progress.PAUSE:
                    break;
                case Progress.WAITING:
                    mNetSpeed.setText("等待中");
                    break;
                case Progress.ERROR:
                    mNetSpeed.setText("等待中");
                    break;
                case Progress.FINISH:
                    mNetSpeed.setText("下载完成");
                    break;
                case Progress.LOADING:
                    String speed = Formatter.formatFileSize(mContext, progress.speed);
                    mNetSpeed.setText(String.format("%s/s", speed));
                    break;
            }
            mNbProgress.setMax(10000);
            mNbProgress.setProgress((int)(progress.fraction * 10000));
        }
    }

    private class DownloadingTaskListener extends DownloadListener {

        private ViewHolder holder;

        DownloadingTaskListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
            DownloadVideoBean bean = (DownloadVideoBean)progress.extra1;
            KLog.i(bean.showName);
        }

        @Override
        public void onProgress(Progress progress) {
            // 作者描述：不仅在任何进度变化的时候会被回调，任何下载状态变化的时候也会回调
            // 所以很多时候，想监听状态变化，也在这个方法中就够了
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            DownloadVideoBean bean = (DownloadVideoBean)progress.extra1;
            KLog.e(bean.showName + "->" + progress);
            Throwable throwable = progress.exception;
            throwable.printStackTrace();
            KLog.e("Exception message: " + throwable.getMessage());
            if (throwable.getMessage().equals(Constants.BREAKPOINT_EXPIRED)) {
                if (tag == holder.getTag()) {
                    KLog.e(bean.showName + "->restart");
                    holder.restart(progress);
                }
            } else if (throwable.getMessage().equals(Constants.BREAKPOINT_NOT_EXIST)) {
                // 断点文件不存在了，可能被人为删除了
                if (tag == holder.getTag()) {
                    KLog.e(bean.showName + "->restart");
                    holder.restart(progress);
                }
            } else if (throwable.getMessage().equals(Constants.UNEXPECTED_END)) {
                if (tag == holder.getTag()) {
                    KLog.e(bean.showName + "->restart");
                    holder.restart(progress);
                }
            }
        }

        @Override
        public void onFinish(File file, Progress progress) {
            DownloadVideoBean bean = (DownloadVideoBean)progress.extra1;
            KLog.i(bean.showName);
            // 下载成功，从taskMap中移除下载成功的，确保taskMap中的为正在下载的，同时移除监听
            mPresenter.removeOneTask(progress.tag);
            updateData();
        }

        @Override
        public void onRemove(Progress progress) {
            DownloadVideoBean bean = (DownloadVideoBean)progress.extra1;
            KLog.i(bean.showName);
        }
    }
}
