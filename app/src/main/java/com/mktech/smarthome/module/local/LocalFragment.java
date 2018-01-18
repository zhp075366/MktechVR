package com.mktech.smarthome.module.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseFragment;
import com.mktech.smarthome.module.local.downloading.DownloadingTaskFragment;
import com.mktech.smarthome.module.local.finished.FinishedTaskFragment;
import com.mktech.smarthome.module.local.local.LocalVideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    public static LocalFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        LocalFragment fragment = new LocalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_local;
    }

    @Override
    protected void createPresenter() {

    }

    private void initTabLayout() {
        List<Fragment> childFragments = new ArrayList<>();
        String[] strTitle = getActivity().getResources().getStringArray(R.array.local_title);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText(strTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setText(strTitle[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(strTitle[2]));
        childFragments.add(DownloadingTaskFragment.newInstance("DownloadingVideo"));
        childFragments.add(FinishedTaskFragment.newInstance("DownloadedVideo"));
        childFragments.add(LocalVideoFragment.newInstance("LocalVideo"));
        FragmentManager fm = this.getChildFragmentManager();
        TitlePagerAdapter adapter = new TitlePagerAdapter(fm, childFragments, strTitle);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(childFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class TitlePagerAdapter extends FragmentPagerAdapter {

        private String[] mTitle;
        private List<Fragment> mFragments;

        public TitlePagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] title) {
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
