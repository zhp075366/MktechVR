package com.mktech.smarthome.module.home;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mktech.smarthome.R;
import com.mktech.smarthome.model.bean.HomeMultipleItemBean;
import com.mktech.smarthome.model.bean.HomePictureBean;


public class HomeMultipleItemAdapter extends BaseMultiItemQuickAdapter<HomeMultipleItemBean, BaseViewHolder> {

    public HomeMultipleItemAdapter() {
        super(null);
        addItemType(HomeMultipleItemBean.TITLE, R.layout.adapter_home_recommend_title_item);
        addItemType(HomeMultipleItemBean.PICTURE, R.layout.adapter_home_picture_item);
        addItemType(HomeMultipleItemBean.WIDE_PICTURE, R.layout.adapter_home_picture_wide_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeMultipleItemBean item) {

        Context context = helper.itemView.getContext();
        HomePictureBean pictureBean = item.getPicture();

        switch (helper.getItemViewType()) {
            case HomeMultipleItemBean.TITLE:
                helper.addOnClickListener(R.id.linear_layout_change);
                helper.setText(R.id.text_view_recomment_title, item.getTitle());
                break;

            case HomeMultipleItemBean.PICTURE:
                ImageView normalImage = helper.getView(R.id.image_view_picture);
                helper.setText(R.id.image_view_main_describe, pictureBean.getMainDescribe());
                helper.setText(R.id.text_view_sub_describe, pictureBean.getSubDescribe());
                Glide.with(context).load(pictureBean.getUrl()).placeholder(R.mipmap.ic_glide_default_image).into(normalImage);
                break;

            case HomeMultipleItemBean.WIDE_PICTURE:
                ImageView Wideimage = helper.getView(R.id.image_view_picture_wide);
                Glide.with(context).load(pictureBean.getUrl()).placeholder(R.mipmap.ic_glide_default_image).into(Wideimage);
                break;
        }
    }
}
