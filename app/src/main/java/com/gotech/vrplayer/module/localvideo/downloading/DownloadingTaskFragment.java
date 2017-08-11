package com.gotech.vrplayer.module.localvideo.downloading;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.utils.DensityUtil;
import com.gotech.vrplayer.widget.CommonLineDivider;
import com.lzy.okserver.download.DownloadTask;

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

    private Context mContext;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        mIsInit = true;
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroyPresenter();
        super.onDestroyView();
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
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new DownloadingTaskPresenter(mContext, this);
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
        int height = (int)DensityUtil.dp2Px(mContext, 1f);
        int padding = (int)DensityUtil.dp2Px(mContext, 5f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        CommonLineDivider itemDecoration = new CommonLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new DownloadingTaskAdapter(mContext, mPresenter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
    }
}
