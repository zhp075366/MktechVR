package com.mktech.smarthome.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.mktech.smarthome.R;
import com.mktech.smarthome.SmartHomeApplication;
import com.mktech.smarthome.module.splash.SplashActivity;
import com.mktech.smarthome.utils.Constants;
import com.mktech.smarthome.utils.SharedPreferenceUtil;
import com.mktech.smarthome.utils.StatusBarUtil;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: BaseActivity中进行P和V的初始化绑定
 */
public abstract class BaseActivity<P> extends AppCompatActivity {

    protected P mPresenter;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needFinishSplash()) {
            KLog.i("needFinishSplash()=true");
            this.finish();
            return;
        }
        checkAppStatus();
        setContentView(getRootLayoutId());
        setStatusBarColor();
        mUnbinder = ButterKnife.bind(this);
        initView();
        // 初始化Presenter和Activity所需数据
        initPresenterData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }

    private void setStatusBarColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_base), 127);
    }

    protected abstract void initView();

    protected abstract int getRootLayoutId();

    protected abstract void initPresenterData();

    private boolean needFinishSplash() {
        String activityName = this.getClass().getSimpleName();
        if (TextUtils.equals(activityName, "SplashActivity")) {
            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                return true;
            }
        }
        return false;
    }

    private void checkAppStatus() {
        String activityName = this.getClass().getSimpleName();
        KLog.i("Current Activity=" + activityName);
        KLog.i("IPCApplication.APP_STATE=" + SmartHomeApplication.APP_STATE);
        // 应用被回收，重启走流程
        if (SmartHomeApplication.APP_STATE == -1) {
            //应用启动入口SplashActivity，走重启流程
            SharedPreferenceUtil.putBoolean(Constants.NAME_APP_KILL, Constants.KEY_APP_KELL, true);
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
