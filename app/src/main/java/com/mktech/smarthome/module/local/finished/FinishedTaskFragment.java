package com.mktech.smarthome.module.local.finished;

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

import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseFragment;
import com.mktech.smarthome.utils.DensityUtil;
import com.mktech.smarthome.widget.SpecialLineDivider;
import com.lzy.okgo.model.Progress;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/7/2
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class FinishedTaskFragment extends BaseFragment<FinishedTaskPresenter> implements IFinishedTaskView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private Context mContext;
    private FinishedTaskAdapter mAdapter;

    // 当前fragment view是否已经初始化
    private boolean mIsInit;
    // 当前fragment view是否可见
    private boolean mIsVisible;

    public static FinishedTaskFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        FinishedTaskFragment fragment = new FinishedTaskFragment();
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
        lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    protected int getRootLayoutId() {
        return R.layout.fragment_downloading_video;
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new FinishedTaskPresenter(mContext, this);
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
        if (!mIsInit || !mIsVisible) {
            return;
        }
        mPresenter.getFinishedTasks();
    }

    @Override
    public void showFinishedTasks(List<Progress> data) {
        if (data.size() == 0) {
            return;
        }
        mAdapter.setNewData(data);
    }

    private void initRecyclerView() {
        int height = (int)DensityUtil.dp2Px(mContext, 0.8f);
        int padding = (int)DensityUtil.dp2Px(mContext, 5f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        SpecialLineDivider itemDecoration = new SpecialLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new FinishedTaskAdapter();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
    }
}
