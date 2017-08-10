package com.gotech.vrplayer.module.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.gotech.vrplayer.R;
import com.gotech.vrplayer.base.BaseActivity;
import com.gotech.vrplayer.module.home.HomeFragment;
import com.gotech.vrplayer.module.localvideo.LocalFragment;
import com.gotech.vrplayer.module.netvideo.VideoFragment;
import com.gotech.vrplayer.module.personal.PersonalFragment;
import com.gotech.vrplayer.utils.ToastUtil;
import com.gotech.vrplayer.widget.VrActionBar;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author: ZouHaiping on 2017/7/3
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class MainActivity extends BaseActivity<MainPresenter> implements
        BottomNavigationBar.OnTabSelectedListener, IMainView,
        EasyPermissions.PermissionCallbacks, View.OnClickListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.action_bar)
    VrActionBar mVrActionBar;

    private Context mContext;
    private Fragment mCurrentFragment;
    private List<Fragment> mTabFragments;

    // 再按一次退出程序
    private long mLastPressTime = 0;
    private static final int mExitTipTime = 1500;
    // 页面类型标识
    private int iPageType = 0;

    // 权限请求码
    private static final int REQUEST_CODE_STORAGE_PERM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
        initNavigationBar();
        requestPermissions();
        mVrActionBar.setOnRightButtonClickListner(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
        // 解决：如果有其它Activity崩溃，MainActivity重建时add Fragment重新加载，HomeFragment出现重叠
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void createPresenter() {
        mContext = this;
        mPresenter = new MainPresenter(this, this);
    }

    @Override
    public void onTabSelected(int position) {
        iPageType = position;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(mCurrentFragment);
        String showTag = mTabFragments.get(position).getClass().getSimpleName();
        Fragment showFragment = manager.findFragmentByTag(showTag);
        if (showFragment == null) {
            showFragment = mTabFragments.get(position);
        }
        if (!showFragment.isAdded()) {
            String tag = showFragment.getClass().getSimpleName();
            transaction.add(R.id.layout_fragment, showFragment, tag);
        } else {
            transaction.show(showFragment);
        }
        transaction.commit();
        mCurrentFragment = showFragment;
        setRightButton(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void initNavigationBar() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        BottomNavigationItem homeItem = new BottomNavigationItem(R.mipmap.ic_main_bottom1, "首页");
        BottomNavigationItem videoItem = new BottomNavigationItem(R.mipmap.ic_main_bottom2, "视频");
        BottomNavigationItem localItem = new BottomNavigationItem(R.mipmap.ic_main_bottom3, "本地");
        BottomNavigationItem personalItem = new BottomNavigationItem(R.mipmap.ic_main_bottom4, "我的");
        mBottomNavigationBar.addItem(homeItem).addItem(videoItem).addItem(localItem).addItem(personalItem);
        mBottomNavigationBar.setActiveColor(R.color.app_base);
        mBottomNavigationBar.setFirstSelectedPosition(0);
        mBottomNavigationBar.initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragments() {
        mTabFragments = new ArrayList<>();
        mTabFragments.add(HomeFragment.newInstance("Home"));
        mTabFragments.add(VideoFragment.newInstance("Video"));
        mTabFragments.add(LocalFragment.newInstance("Local"));
        mTabFragments.add(PersonalFragment.newInstance("Personal"));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mCurrentFragment = mTabFragments.get(0);
        String tag = mCurrentFragment.getClass().getSimpleName();
        transaction.add(R.id.layout_fragment, mCurrentFragment, tag);
        transaction.commit();
    }

    private void setRightButton(int position) {
        switch (position) {
            case 0:
                mVrActionBar.setRightButton(VrActionBar.RIGHT_BUTTON.SEARCH_ICON);
                break;
            case 1:
                mVrActionBar.setRightButton(VrActionBar.RIGHT_BUTTON.SEARCH_ICON);
                break;
            case 2:
                mVrActionBar.setRightButton(VrActionBar.RIGHT_BUTTON.EDIT_ICON);
                break;
            case 3:
                mVrActionBar.setRightButton(VrActionBar.RIGHT_BUTTON.NONE_ICON);
                break;
        }
    }

    public void requestPermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.request_storage_tip), REQUEST_CODE_STORAGE_PERM, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        KLog.i("onPermissionsGranted requestCode=" + requestCode);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        KLog.e("onPermissionsDenied requestCode=" + requestCode);
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle(getString(R.string.permission_request_title))
                    .setRationale(getString(R.string.permission_request_content))
                    .setNegativeButton(getString(R.string.cancel))
                    .setPositiveButton(getString(R.string.setting))
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from system settings screen, like showing a Toast.
            KLog.i("Returned from system settings to MainActivity");
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mLastPressTime) > mExitTipTime) {
            ToastUtil.showToast(mContext, R.string.exit_app_tip, Toast.LENGTH_SHORT);
            mLastPressTime = System.currentTimeMillis();
        } else {
            ToastUtil.cancelToast();
            this.finish();
        }
    }

    private void checkUpdate() {
        mPresenter.checkUpdate();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.actionbar_search) {
            switch (iPageType) {
                case 0:
                case 1:
                    break;
                case 2:
                    break;
            }
        }
    }
}
