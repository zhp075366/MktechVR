package com.mktech.smarthome.module.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseFragment;
import com.mktech.smarthome.model.bean.HomeCategoryBean;
import com.mktech.smarthome.model.bean.HomeMultipleItemBean;
import com.mktech.smarthome.module.video.detail.VideoDetailActivity;
import com.mktech.smarthome.utils.DensityUtil;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    private int mRecommendID;
    private LayoutInflater mInflater;
    private HomeMultipleItemAdapter mAdapter;

    public static HomeFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initPresenterData() {
        mPresenter = new HomePresenter(mActivity, this);
        mPresenter.getFirstLoadData();
    }

    @Override
    protected void initView() {
        mInflater = mActivity.getLayoutInflater();
        initRecyclerView();
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroyPresenter();
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

    // 换一换要用到，需要得到换哪一组数据
    @Override
    public int getRecommendID() {
        return mRecommendID;
    }

    @Override
    public void showFirstLoadData(final List<HomeMultipleItemBean> data) {
        addViewPagerHeader();
        addCategoryHeader();
        addSpaceHeader();
        addNoMoreFooter();
        setItemClickListener(data);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        mAdapter.setNewData(data);
        //mPresenter.addTask();
        //mAppUpdateManager.checkUpdate(true);
    }

    private void initRecyclerView() {
        int itemPadding = (int)DensityUtil.dp2Px(mActivity, 1);
        int bottomPadding = (int)DensityUtil.dp2Px(mActivity, 1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        HomeItemDecoration itemDecoration = new HomeItemDecoration(itemPadding, bottomPadding);
        mAdapter = new HomeMultipleItemAdapter();
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setItemClickListener(final List<HomeMultipleItemBean> data) {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 此position不包括header和footer,和data list保持一致
                KLog.i("onItemClick position=" + position);
                if (data.get(position).getPicture() != null) {
                    startActivity(new Intent(mActivity, VideoDetailActivity.class));
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 此position不包括header和footer,和data list 保持一致
                KLog.i("onItemChildClick position=" + position);
                KLog.i("Title:" + data.get(position).getTitle());
                List<HomeMultipleItemBean> changeList = mPresenter.getReplaceRecommendData();
                if (!(adapter instanceof HomeMultipleItemAdapter)) {
                    return;
                }

                for (int index = 0; index < changeList.size(); index++) {
                    // +1 从下一个Item开始更新
                    int pos = position + index + 1;
                    adapter.setData(pos, changeList.get(index));
                }
            }
        });
    }

    private void addViewPagerHeader() {
        final RollPagerView viewPager = new RollPagerView(mActivity);
        viewPager.setHintView(new ColorPointHintView(mActivity, Color.YELLOW, Color.GRAY));
        viewPager.setHintPadding(0, 0, 0, (int)DensityUtil.dp2Px(mActivity, 4));
        viewPager.setPlayDelay(2000);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = (int)DensityUtil.dp2Px(mActivity, 180);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(viewPager, mPresenter.getViewPagerData());
        viewPager.setAdapter(adapter);
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        viewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                KLog.i("viewPager Item " + position + " clicked");
            }
        });
        mAdapter.addHeaderView(viewPager);
    }

    private void addCategoryHeader() {
        RecyclerView recyclerView = new RecyclerView(mActivity);
        HomeCategoryAdapter adapter = new HomeCategoryAdapter();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = (int)DensityUtil.dp2Px(mActivity, 80);
        final List<HomeCategoryBean> categoryData = mPresenter.getCategoryData();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 5);

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        adapter.setNewData(categoryData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.i("categoryData Click:" + categoryData.get(position).getType());
            }
        });

        mAdapter.addHeaderView(recyclerView);
    }

    private void addSpaceHeader() {
        final View view = new View(mActivity);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = (int)DensityUtil.dp2Px(mActivity, 8);
        view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        view.setBackgroundColor(getResources().getColor(R.color.gainsboro));
        mAdapter.addHeaderView(view);
    }

    private void addNoMoreFooter() {
        View view = mInflater.inflate(R.layout.layout_load_no_more, (ViewGroup)mRecyclerView.getParent(), false);
        mAdapter.addFooterView(view);
    }
}
