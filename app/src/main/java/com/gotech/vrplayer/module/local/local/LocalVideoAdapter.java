package com.gotech.vrplayer.module.local.local;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.model.bean.LocalVideoBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/14
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoAdapter extends BaseQuickAdapter<LocalVideoBean, BaseViewHolder> {

    private Bitmap[] mThumbnailBitmaps;
    private LocalVideoPresenter mPresenter;

    public LocalVideoAdapter(LocalVideoPresenter presenter) {
        super(R.layout.adapter_local_video_item);
        mPresenter = presenter;
    }

    public void setData(List<LocalVideoBean> data) {
        super.setNewData(data);
        mThumbnailBitmaps = new Bitmap[data.size()];
        mPresenter.initThumbnailTask(data.size());
    }

    public void setThumbnail(int position, Bitmap bitmap) {
        mThumbnailBitmaps[position] = bitmap;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalVideoBean item) {
        int position = helper.getAdapterPosition();
        String resString = mContext.getResources().getString(R.string.local_video_size_duration);
        String sizeDuration = String.format(resString, item.getSize(), item.getDuration());
        helper.setText(R.id.tv_title, item.getDisplayName());
        helper.setText(R.id.tv_size_duration, sizeDuration);
        helper.setText(R.id.tv_path, item.getPath());
        ImageView thumbnailView = helper.getView(R.id.iv_thumbnail);
        if (mThumbnailBitmaps[position] == null) {
            thumbnailView.setImageResource(R.mipmap.ic_local_video_default);
            mPresenter.getThumbnail(position, item.getPath());
        } else {
            thumbnailView.setImageBitmap(mThumbnailBitmaps[position]);
        }
    }
}
