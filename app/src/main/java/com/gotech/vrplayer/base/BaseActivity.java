package com.gotech.vrplayer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: BaseActivity中进行P和V的初始化绑定，并自动调用startPresenter, destroyPresenter
 */
public abstract class BaseActivity<P extends IBasePresenter> extends AppCompatActivity {

    protected P mPresenter;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getRootLayoutId());
        setStatusBarColor();
        mUnbinder = ButterKnife.bind(this);
        createPresenter();
        startPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.destroyPresenter();
        }
    }

    private void setStatusBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_base), 127);
    }

    private void startPresenter() {
        if (mPresenter != null) {
            mPresenter.startPresenter();
        }
    }

    protected abstract int getRootLayoutId();

    protected abstract void createPresenter();
}
