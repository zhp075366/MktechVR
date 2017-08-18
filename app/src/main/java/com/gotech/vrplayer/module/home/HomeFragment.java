package com.gotech.vrplayer.module.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseFragment;
import com.gotech.vrplayer.model.bean.HomeCategoryBean;
import com.gotech.vrplayer.model.bean.HomeMultipleItemBean;
import com.gotech.vrplayer.module.personal.update.AppUpdateManager;
import com.gotech.vrplayer.module.personal.update.AppUpdateService;
import com.gotech.vrplayer.module.video.detail.VideoDetailActivity;
import com.gotech.vrplayer.utils.DensityUtil;
import com.gotech.vrplayer.utils.NetworkUtil;
import com.gotech.vrplayer.utils.ToastUtil;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
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
    private Context mContext;
    private LayoutInflater mInflater;
    private HomeMultipleItemAdapter mAdapter;

    // APK更新管理器
    private AppUpdateManager mAppUpdateManager;

    public static HomeFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initUpdateManager();
        mPresenter.getFirstLoadData();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroyView() {
        mPresenter.destroyPresenter();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.setUIHandler(null);
            AppUpdateService.UPDATE_SERVICE_STATE eState = mAppUpdateManager.getServiceState();
            if (eState != AppUpdateService.UPDATE_SERVICE_STATE.DOWNLOADINIG) {
                mAppUpdateManager.stopUpdateService();
            }
            mAppUpdateManager.unbindUpdateService();
        }
        mUIHandler.removeCallbacksAndMessages(null);
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
        checkUpdate();
    }

    @Override
    protected void createPresenter() {
        mContext = getContext();
        mPresenter = new HomePresenter(mContext, this);
    }

    private void initRecyclerView() {
        int itemPadding = (int)DensityUtil.dp2Px(mContext, 1);
        int bottomPadding = (int)DensityUtil.dp2Px(mContext, 1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
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
                    startActivity(new Intent(mContext, VideoDetailActivity.class));
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
        final RollPagerView viewPager = new RollPagerView(mContext);
        viewPager.setHintView(new ColorPointHintView(mContext, Color.YELLOW, Color.GRAY));
        viewPager.setHintPadding(0, 0, 0, (int)DensityUtil.dp2Px(mContext, 4));
        viewPager.setPlayDelay(2000);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = (int)DensityUtil.dp2Px(mContext, 180);
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
        RecyclerView recyclerView = new RecyclerView(mContext);
        HomeCategoryAdapter adapter = new HomeCategoryAdapter();
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = (int)DensityUtil.dp2Px(mContext, 80);
        final List<HomeCategoryBean> categoryData = mPresenter.getCategoryData();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 5);

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
        final View view = new View(mContext);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = (int)DensityUtil.dp2Px(mContext, 8);
        view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        view.setBackgroundColor(getResources().getColor(R.color.gainsboro));
        mAdapter.addHeaderView(view);
    }

    private void addNoMoreFooter() {
        View view = mInflater.inflate(R.layout.layout_load_no_more, (ViewGroup)mRecyclerView.getParent(), false);
        mAdapter.addFooterView(view);
    }

    private void initUpdateManager() {
        mAppUpdateManager = new AppUpdateManager(mContext);
        mAppUpdateManager.startUpdateService();
        mAppUpdateManager.bindUpdateService();
    }

    public void checkUpdate() {
        if (!NetworkUtil.checkNetworkConnection(mContext)) {
            return;
        }
        AppUpdateService.UPDATE_SERVICE_STATE eState = mAppUpdateManager.getServiceState();
        if (eState == AppUpdateService.UPDATE_SERVICE_STATE.CHECKING) {
            return;
        } else if (eState == AppUpdateService.UPDATE_SERVICE_STATE.DOWNLOADINIG) {
            return;
        }
        // 此Handler用于Service检测更新/下载更新结果回调通知
        mAppUpdateManager.setUIHandler(mUIHandler);
        mAppUpdateManager.checkUpdate();
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppUpdateService.AUTO_UPDATE_CHECKING_COMPLETE:
                    AppUpdateService.CheckUpdateMsg updateMsg = (AppUpdateService.CheckUpdateMsg)msg.obj;
                    if (updateMsg.eResult == AppUpdateService.CHECK_UPDATE_RESULT.HAVE_UPDATE) {
                        mAppUpdateManager.saveAppInfo(updateMsg.strAppMd5, updateMsg.appSize);
                        mAppUpdateManager.showUpdateDialog(updateMsg.strCheckResult);
                    }
                    break;
                case AppUpdateService.AUTO_UPDATE_DOWNLOADING_COMPLETE:
                    AppUpdateService.DownloadUpdateMsg downloadMsg = (AppUpdateService.DownloadUpdateMsg)msg.obj;
                    ToastUtil.showToast(mContext, downloadMsg.strDownloadResult, Toast.LENGTH_LONG);
                    break;
            }
        }
    };
}
