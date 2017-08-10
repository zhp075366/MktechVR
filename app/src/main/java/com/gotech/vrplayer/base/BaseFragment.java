package com.gotech.vrplayer.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者：Zou Haiping on 2016/10/9 10:50
 * 邮箱：zhp075366@163.com
 * 公司：MKTech
 */
public abstract class BaseFragment<P extends IBasePresenter> extends Fragment {

    protected P mPresenter;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getRootLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createPresenter();
        startPresenter();
    }

    protected abstract int getRootLayoutId();

    protected abstract void createPresenter();

    private void startPresenter() {
        if (mPresenter != null) {
            mPresenter.startPresenter();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.destroyPresenter();
        }
    }
}
