package com.mktech.smarthome.module.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mktech.smarthome.R;
import com.mktech.smarthome.model.bean.HomeAdBean;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

public class HomeViewPagerAdapter extends LoopPagerAdapter {

    private List<HomeAdBean> mData;

    public HomeViewPagerAdapter(RollPagerView viewPager, List<HomeAdBean> list) {
        super(viewPager);
        mData = list;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //加载图片
        Glide.with(container.getContext()).load(mData.get(position).getUrl()).placeholder(R.mipmap.ic_glide_default_image).into(imageView);
        return imageView;
    }

    @Override
    public int getRealCount() {
        return mData.size();
    }
}