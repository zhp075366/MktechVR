package com.mktech.smarthome.module.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.mktech.smarthome.R;
import com.mktech.smarthome.base.BaseActivity;

/**
 * Author: weih on 2017/7/6.
 * E-mail: wei.huang@gotech.cn
 * Desc:
 */
public class VrPlayerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = getIntent().getStringExtra("URL");
        //addFragment(R.id.id_fragment_container, VrPlayerFragment.newInstance(url), VrPlayerFragment.TAG);
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_vr_player;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenterData() {

    }

    public static void startVrPlayerActivity(Context context, String url) {
        Intent intent = new Intent(context, VrPlayerActivity.class);
        intent.putExtra("URL", url);
        context.startActivity(intent);
    }

    private void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}
