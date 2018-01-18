package com.mktech.smarthome.module.local.local;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mktech.smarthome.R;
import com.mktech.smarthome.model.bean.LocalVideoBean;
import com.mktech.smarthome.utils.Constants;

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
        mThumbnailBitmaps = new Bitmap[data.size()];
        mPresenter.initLoadThumbnailTask(data.size());
        super.setNewData(data);
    }

    public void setThumbnail(int position, Bitmap bitmap) {
        mThumbnailBitmaps[position] = bitmap;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalVideoBean item) {
        int position = helper.getAdapterPosition();
        String formatStr, sizeStr;
        if (item.getSize() > Constants.ONE_MB_SIZE) {
            formatStr = mContext.getResources().getString(R.string.local_video_size_duration_in_mb);
            sizeStr = Constants.TWO_DECIMAL_FORMAT.format((double)item.getSize() / 1024 / 1024);
        } else {
            formatStr = mContext.getResources().getString(R.string.local_video_size_duration_in_kb);
            sizeStr = Constants.TWO_DECIMAL_FORMAT.format((double)item.getSize() / 1024);
        }
        String durationStr = Constants.ONE_DECIMAL_FORMAT.format((double)item.getDuration() / 60 / 60);
        String sizeDurationStr = String.format(formatStr, sizeStr, durationStr);
        helper.setText(R.id.tv_title, item.getDisplayName());
        helper.setText(R.id.tv_size_duration, sizeDurationStr);
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
