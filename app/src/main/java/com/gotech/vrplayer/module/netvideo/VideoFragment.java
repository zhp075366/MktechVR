package com.gotech.vrplayer.module.netvideo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.model.bean.VideoChannelBean;
import com.gotech.vrplayer.utils.DensityUtil;
import com.gotech.vrplayer.widget.colortrackview.ColorTrackTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    ColorTrackTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private Context mContext;

    public static VideoFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        initChildFragments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void createPresenter() {

    }

    private void initChildFragments() {
        List<Fragment> childFragments = new ArrayList<>();
        String[] strTitle = getActivity().getResources().getStringArray(R.array.video_channel_title);
        String[] strCode = getActivity().getResources().getStringArray(R.array.video_channel_code);
        for(int i = 0; i < strTitle.length; i++) {
            VideoChannelBean channel = new VideoChannelBean(strTitle[i], strCode[i]);
            childFragments.add(VideoChannelFragment.newInstance(channel));
        }
        FragmentManager fm = this.getChildFragmentManager();
        ChannelPagerAdapter adapter = new ChannelPagerAdapter(fm, childFragments, strTitle);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(childFragments.size());
        int left = (int)DensityUtil.dp2Px(mContext, 10);
        int right = (int)DensityUtil.dp2Px(mContext, 10);
        mTabLayout.setTabPaddingLeftAndRight(left, right);
        mTabLayout.setLastSelectedTabPosition(0);
        //mTabLayout.setSelectedTabIndicatorHeight(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class ChannelPagerAdapter extends FragmentStatePagerAdapter {

        private String[] mTitle;
        private List<Fragment> mFragments;

        public ChannelPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] title) {
            super(fm);
            mTitle = title;
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }
    }
}
