package com.mktech.smarthome.module.video.detail;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mktech.smarthome.R;
import com.mktech.smarthome.model.bean.HomePictureBean;

/**
 * Author: ZouHaiping on 2017/6/12
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoDetailAdapter extends BaseQuickAdapter<HomePictureBean, BaseViewHolder> {

    public VideoDetailAdapter() {
        super(R.layout.adapter_video_detail_item);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, HomePictureBean item) {
        helper.setText(R.id.text_view_main_describe, item.getMainDescribe());
        helper.setText(R.id.text_view_sub_describe, item.getSubDescribe());

        Context context = helper.itemView.getContext();
        ImageView image = helper.getView(R.id.image_view_picture);
        Glide.with(context).load(item.getUrl()).placeholder(R.mipmap.ic_glide_default_image).into(image);
    }
}
