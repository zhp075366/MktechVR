package com.gotech.vrplayer.module.local.finished;

import android.text.format.Formatter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.model.bean.DownloadVideoBean;
import com.gotech.vrplayer.widget.NumberProgressBar;
import com.lzy.okgo.model.Progress;

/**
 * Author: ZouHaiping on 2017/7/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 下载信息(Progress)，存储在数据库的信息作为Item
 */
public class FinishedTaskAdapter extends BaseQuickAdapter<Progress, BaseViewHolder> {

    public FinishedTaskAdapter() {
        super(R.layout.adapter_finished_task_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, Progress item) {
        DownloadVideoBean bean = (DownloadVideoBean)item.extra1;
        String currentSize = Formatter.formatFileSize(mContext, item.currentSize);
        String totalSize = Formatter.formatFileSize(mContext, item.totalSize);
        holder.setText(R.id.name, bean.showName);
        holder.setText(R.id.downloadSize, currentSize + "/" + totalSize);
        ImageView ivIcon = holder.getView(R.id.icon);
        NumberProgressBar progressBar = holder.getView(R.id.nbProgress);
        progressBar.setMax(10000);
        progressBar.setProgress((int)(item.fraction * 10000));
        Glide.with(mContext).load(bean.iconUrl).placeholder(R.mipmap.ic_launcher).into(ivIcon);
    }
}
