package com.mktech.smarthome.module.personal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseFragment;
import com.mktech.smarthome.module.rpc.GRPCTestActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: ZouHaiping on 2017/6/27
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class PersonalFragment extends BaseFragment<PersonalPresenter> implements IPersonalView {

    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;

    public static PersonalFragment newInstance(String arg) {
        Bundle args = new Bundle();
        args.putString("ARGS", arg);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initPresenterData() {
        mPresenter = new PersonalPresenter(mActivity, this);
        showVersionName();
    }

    private void showVersionName() {
        String appVersion = mPresenter.getVersionName();
        mTvAppVersion.setText(appVersion);
    }

    @OnClick(R.id.grpc_test)
    public void grpcTest() {
        startActivity(new Intent(mActivity, GRPCTestActivity.class));
    }

    @OnClick(R.id.huawei_message_test)
    public void messageTest() {

    }
}
