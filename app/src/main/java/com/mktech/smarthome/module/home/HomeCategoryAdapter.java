package com.mktech.smarthome.module.home;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mktech.smarthome.R;
import com.mktech.smarthome.model.bean.HomeCategoryBean;

/**
 * 作者：Created by ZouHaiping on 2017/6/7
 * 邮箱：haiping.zou@gotechcn.cn
 * 公司：MKTech
 */
public class HomeCategoryAdapter extends BaseQuickAdapter<HomeCategoryBean, BaseViewHolder> {

    public HomeCategoryAdapter() {
        super(R.layout.adapter_home_category_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeCategoryBean item) {
        helper.setText(R.id.text_view_text, item.getType());
        helper.setImageResource(R.id.image_view_icon, item.getImageId());
    }
}
