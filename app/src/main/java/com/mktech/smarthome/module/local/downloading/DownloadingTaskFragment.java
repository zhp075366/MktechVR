package com.mktech.smarthome.module.local.downloading;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.lzy.okserver.download.DownloadTask;
import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseFragment;
import com.mktech.smarthome.utils.DensityUtil;
import com.mktech.smarthome.widget.CommonLineDivider;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/7/2
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class DownloadingTaskFragment extends BaseFragment<DownloadingTaskPresenter> implements IDownloadingTaskView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private DownloadingTaskAdapter mAdapter;

    // 当前fragment view是否已经初始化
    private boolean mIsInit;
    // 当前fragment view是否可见
    private boolean mIsVisible;

    public static DownloadingTaskFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        DownloadingTaskFragment fragment = new DownloadingTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroyPresenter();
        super.onDestroyView();
    }

    @Override
    protected void initView() {
        initRecyclerView();
        mIsInit = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_downloading_video;
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
    protected void initPresenterData() {
        mPresenter = new DownloadingTaskPresenter(mActivity, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            lazyLoad();
        } else {
            mIsVisible = false;
            // 当前页面被切走
            if (mIsInit) {
                mPresenter.unRegisterListener();
            }
        }
    }

    private void lazyLoad() {
        if (!mIsInit || !mIsVisible) {
            return;
        }
        // 从数据库中恢复未完成的任务并存在了OkDownload中的taskMap
        mPresenter.restoreDownloadingTasks();
    }

    @Override
    public void showDownloadingTasks(List<DownloadTask> data) {
        mAdapter.setData(data);
        if (data.size() == 0) {
            return;
        }
        // OkDownload就可以直接启动taskMap中的任务
        mPresenter.startAllTasks();
    }

    private void initRecyclerView() {
        int height = (int)DensityUtil.dp2Px(mActivity, 0.8f);
        int padding = (int)DensityUtil.dp2Px(mActivity, 5f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        CommonLineDivider itemDecoration = new CommonLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new DownloadingTaskAdapter(mActivity, mPresenter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
    }
}
