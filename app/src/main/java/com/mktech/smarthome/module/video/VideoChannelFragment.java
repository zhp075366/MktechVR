package com.mktech.smarthome.module.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseFragment;
import com.mktech.smarthome.model.bean.HomePictureBean;
import com.mktech.smarthome.model.bean.VideoChannelBean;
import com.mktech.smarthome.module.video.detail.VideoDetailActivity;
import com.mktech.smarthome.utils.DensityUtil;
import com.mktech.smarthome.widget.CommonLoadMoreView;
import com.mktech.smarthome.widget.SpecialLineDivider;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoChannelFragment extends BaseFragment<VideoChannelPresenter> implements IVideoChannelView, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private int nPage = 0;
    private VideoChannelAdapter mAdapter;

    // 当前fragment view是否已经初始化
    private boolean mIsInit;
    // 当前fragment view是否可见
    private boolean mIsVisible;
    // 第一次加载数据是否成功
    private boolean mHasFirstLoaded;

    private VideoChannelBean mChannel;

    public static VideoChannelFragment newInstance(VideoChannelBean channel) {
        Bundle args = new Bundle();
        args.putParcelable("ARGS", channel);
        VideoChannelFragment fragment = new VideoChannelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        initRecyclerView();
        mIsInit = true;
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroyPresenter();
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            lazyLoad();
        } else {
            mIsVisible = false;
        }
    }

    private void lazyLoad() {
        if (!mIsInit || !mIsVisible || mHasFirstLoaded) {
            return;
        }
        mPresenter.getFirstLoadData();
    }

    @Override
    public void showLoading() {
        mLoadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingProgress.setVisibility(View.GONE);
    }

    @Override
    public int getLoadPageNum() {
        return nPage;
    }

    @Override
    public String getChannelCode() {
        return mChannel.getTitleCode();
    }

    @Override
    public void showNetError() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void showFirstLoadData(List<HomePictureBean> data) {
        mHasFirstLoaded = true;
        setItemClickListener(data);
        mAdapter.setNewData(data);
        nPage++;
    }

    @Override
    public void showLoadMoreData(List<HomePictureBean> data) {
        mAdapter.loadMoreComplete();
        if (data == null) {
            mAdapter.loadMoreEnd();
            return;
        }
        mAdapter.addData(data);
        nPage++;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_video_channel;
    }

    @Override
    protected void initPresenterData() {
        mChannel = getArguments().getParcelable("ARGS");
        mPresenter = new VideoChannelPresenter(mActivity, this);
        lazyLoad();
    }

    @Override
    public void onLoadMoreRequested() {
        KLog.i("onLoadMoreRequested...");
        mPresenter.loadMore();
    }

    private void setItemClickListener(final List<HomePictureBean> data) {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 此position不包括header和footer,和data list保持一致
                KLog.i("onItemClick position=" + position + " " + data.get(position).getMainDescribe());
                startActivity(new Intent(mActivity, VideoDetailActivity.class));
            }
        });
    }

    private void initRecyclerView() {
        int height = (int)DensityUtil.dp2Px(mActivity, 0.8f);
        int padding = (int)DensityUtil.dp2Px(mActivity, 10f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        SpecialLineDivider itemDecoration = new SpecialLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(true);
        CommonLoadMoreView loadMoreView = new CommonLoadMoreView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new VideoChannelAdapter();
        // 设置预加载
        mAdapter.setPreLoadNumber(8);
        mAdapter.setLoadMoreView(loadMoreView);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
