package com.mktech.smarthome.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.mktech.smarthome.R;
import com.mktech.smarthome.SmartHomeApplication;
import com.mktech.smarthome.base.BaseActivity;
import com.mktech.smarthome.module.main.MainActivity;
import com.mktech.smarthome.utils.StatusBarUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.scale_text)
    TextView mScaleText;

    @Override
    protected void initView() {
        StatusBarUtil.removeStatusBarView(SplashActivity.this);
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenterData() {
        scaleAnimation();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 进入应用APP_STATE改成正常态1
        SmartHomeApplication.APP_STATE = 1;
        super.onCreate(savedInstanceState);
    }

    private void scaleAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);//设置动画持续时间
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        mScaleText.setAnimation(animation);
        animation.startNow();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gotoHome();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void gotoHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
