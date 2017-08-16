package com.gotech.vrplayer.module.local.local;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.model.bean.LocalVideoBean;
import com.gotech.vrplayer.module.player.VrPlayerActivity;
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
    // 第一次加载数据是否成功
    private boolean mHasFirstLoaded;

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
        if (!mIsInit || !mIsVisible || mHasFirstLoaded) {
            return;
        }
        mPresenter.getAllVideo();
    }

    @Override
    public void showLocalVideo(List<LocalVideoBean> data) {
        mHasFirstLoaded = true;
        if (data.size() == 0) {
            return;
        }
        setItemClickListener(data);
        mAdapter.setData(data);
    }

    @Override
    public void setThumbnail(int position, Bitmap bitmap) {
        mAdapter.setThumbnail(position, bitmap);
    }

    private void setItemClickListener(final List<LocalVideoBean> data) {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 此position不包括header和footer,和data list保持一致
                KLog.i("onItemClick position=" + position + " " + data.get(position).getPath());
                //VrPlayerActivity.startVrPlayerActivity(mContext, data.get(position).getPath());
            }
        });
    }

    private void initRecyclerView() {
        int height = (int)DensityUtil.dp2Px(mContext, 0.8f);
        int padding = (int)DensityUtil.dp2Px(mContext, 5f);
        // 分割线颜色 & 高度 & 左边距 & 右边距
        SpecialLineDivider itemDecoration = new SpecialLineDivider(Color.LTGRAY, height, padding, padding);
        itemDecoration.setDrawLastItem(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mAdapter = new LocalVideoAdapter(mPresenter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(itemDecoration);
    }
}
