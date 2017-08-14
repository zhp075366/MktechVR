package com.gotech.vrplayer.module.local.local;

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
import com.gotech.vrplayer.model.bean.LocalVideoBean;
import com.gotech.vrplayer.utils.DensityUtil;
import com.gotech.vrplayer.widget.SpecialLineDivider;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/7/2
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoFragment extends BaseFragment<LocalVideoPresenter> implements ILocalVideoView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private Context mContext;
    private LocalVideoAdapter mAdapter;

    // 当前fragment view是否已经初始化
    private boolean mIsInit;
    // 当前fragment view是否可见
    private boolean mIsVisible;

    public static LocalVideoFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        LocalVideoFragment fragment = new LocalVideoFragment();
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
        mPresenter.destroyPresenter();
        super.onDestroyView();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_local_video;
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new LocalVideoPresenter(mContext, this);
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
        mPresenter.getAllVideo();
    }

    @Override
    public void showLocalVideo(List<LocalVideoBean> data) {
        KLog.i("showLocalVideo size=" + data.size());
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
        mAdapter = new LocalVideoAdapter();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
    }
}
