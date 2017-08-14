package com.gotech.vrplayer.module.local.local;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.model.bean.LocalVideoBean;

/**
 * Author: ZouHaiping on 2017/8/14
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoAdapter extends BaseQuickAdapter<LocalVideoBean, BaseViewHolder> {

    public LocalVideoAdapter() {
        super(R.layout.adapter_local_video_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalVideoBean item) {
        String resString = mContext.getResources().getString(R.string.local_video_size_duration);
        String sizeDuration = String.format(resString, item.getSize(), item.getDuration());
        helper.setText(R.id.tv_title, item.getDisplayName());
        helper.setText(R.id.tv_size_duration, sizeDuration);
        helper.setText(R.id.tv_path, item.getPath());
    }
}
