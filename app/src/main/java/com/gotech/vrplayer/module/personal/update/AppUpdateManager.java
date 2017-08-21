package com.gotech.vrplayer.module.personal.update;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.AppUtil;
import com.gotech.vrplayer.utils.NetworkUtil;
import com.gotech.vrplayer.utils.ToastUtil;
import com.gotech.vrplayer.widget.CustomDialog;

public class AppUpdateManager implements OnClickListener {

    private Context mContext;
    private Resources mResources;
    private Dialog mCheckingDialog;
    private Dialog mDownloadDialog;
    private AppUpdateService mAppUpdateService;

    private int mAppSize;
    private String mAppMD5;
    // 打开App默认为Home Check一次
    private boolean mIsHomeCheck = true;

    public void init(Context context) {
        mContext = context;
        mResources = mContext.getResources();
        startUpdateService();
        bindUpdateService();
    }

    public void destroy() {
        mAppUpdateService.setUIHandler(null);
        mUIHandler.removeCallbacksAndMessages(null);
        AppUpdateService.UPDATE_SERVICE_STATE eState = mAppUpdateService.getServiceState();
        unbindUpdateService();
        if (eState != AppUpdateService.UPDATE_SERVICE_STATE.DOWNLOADINIG) {
            stopUpdateService();
        }
    }

    public void checkUpdate(boolean isHomeCheck) {
        mIsHomeCheck = isHomeCheck;
        if (!NetworkUtil.checkNetworkConnection(mContext)) {
            if (!mIsHomeCheck) {
                ToastUtil.showToast(mContext, R.string.no_network_connect, Toast.LENGTH_SHORT);
            }
            return;
        }
        AppUpdateService.UPDATE_SERVICE_STATE eState = mAppUpdateService.getServiceState();
        if (eState == AppUpdateService.UPDATE_SERVICE_STATE.DOWNLOADINIG) {
            if (!mIsHomeCheck) {
                ToastUtil.showToast(mContext, R.string.update_downloading, Toast.LENGTH_SHORT);
            }
            return;
        }
        if (eState == AppUpdateService.UPDATE_SERVICE_STATE.CHECKING) {
            // 如果首页先检测，Setting后检测，直接沿用这次的检测
            if (!mIsHomeCheck) {
                showCheckingDialog(mResources.getString(R.string.update_check_tips));
            }
            // 如果Setting先检测，首页后检测，直接return
            return;
        }
        if (!mIsHomeCheck) {
            showCheckingDialog(mResources.getString(R.string.update_check_tips));
        }
        mAppUpdateService.setServiceState(AppUpdateService.UPDATE_SERVICE_STATE.CHECKING);
        mAppUpdateService.startCheckUpdate();
    }

    private ServiceConnection onUpdateServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mAppUpdateService = ((AppUpdateService.UpdateServiceBinder)binder).getService();
            mAppUpdateService.setUIHandler(mUIHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAppUpdateService = null;
        }
    };

    private void startUpdateService() {
        if (AppUtil.isServiceRunning(mContext, AppUpdateService.class.getName())) {
            return;
        }
        Intent intent = new Intent(mContext, AppUpdateService.class);
        mContext.startService(intent);
    }

    private void stopUpdateService() {
        if (!AppUtil.isServiceRunning(mContext, AppUpdateService.class.getName())) {
            return;
        }
        Intent intent = new Intent(mContext, AppUpdateService.class);
        mContext.stopService(intent);
    }

    private void unbindUpdateService() {
        mContext.unbindService(onUpdateServiceConnection);
        mAppUpdateService = null;
    }

    private void bindUpdateService() {
        Intent intentBackupService = new Intent(mContext, AppUpdateService.class);
        mContext.bindService(intentBackupService, onUpdateServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.okButton:
                dismissDownloadDialog();
                mAppUpdateService.initNotification();
                mAppUpdateService.startDownloadUpdate(mAppMD5, mAppSize);
                mAppUpdateService.setServiceState(AppUpdateService.UPDATE_SERVICE_STATE.DOWNLOADINIG);
                ToastUtil.showToast(mContext, R.string.update_start_download, Toast.LENGTH_SHORT);
                break;
            case R.id.cancelButton:
                dismissDownloadDialog();
                mAppUpdateService.setServiceState(AppUpdateService.UPDATE_SERVICE_STATE.IDLE);
                break;
        }
    }

    private void showDownloadDialog(String updateInfo) {
        dismissDownloadDialog();
        View view = View.inflate(mContext, R.layout.dialog_update_tip, null);
        TextView textContent = (TextView)view.findViewById(R.id.text_content);
        textContent.setText(updateInfo);
        Button btnOK = (Button)view.findViewById(R.id.okButton);
        Button btnCancel = (Button)view.findViewById(R.id.cancelButton);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        mDownloadDialog = new CustomDialog(mContext, view, R.style.UpdateDialog);
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();
    }

    private void dismissDownloadDialog() {
        if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
            mDownloadDialog.dismiss();
            mDownloadDialog = null;
        }
    }

    private void showCheckingDialog(String tipInfo) {
        dismissCheckingDialog();
        View view = View.inflate(mContext, R.layout.dialog_check_update, null);
        TextView textTip = (TextView)view.findViewById(R.id.tvTip);
        textTip.setText(tipInfo);
        mCheckingDialog = new CustomDialog(mContext, view, R.style.WaitDialog);
        mCheckingDialog.setCancelable(false);
        mCheckingDialog.show();
    }

    private void dismissCheckingDialog() {
        if (mCheckingDialog != null && mCheckingDialog.isShowing()) {
            mCheckingDialog.dismiss();
            mCheckingDialog = null;
        }
    }

    private void saveDownloadAppInfo(String appMd5, int fileLength) {
        mAppMD5 = appMd5;
        mAppSize = fileLength;
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppUpdateService.AUTO_UPDATE_CHECKING_COMPLETE:
                    dismissCheckingDialog();
                    AppUpdateService.CheckUpdateMsg updateMsg = (AppUpdateService.CheckUpdateMsg)msg.obj;
                    if (updateMsg.eResult == AppUpdateService.CHECK_UPDATE_RESULT.HAVE_UPDATE) {
                        saveDownloadAppInfo(updateMsg.strAppMd5, updateMsg.appSize);
                        showDownloadDialog(updateMsg.strCheckResult);
                    } else if (updateMsg.eResult == AppUpdateService.CHECK_UPDATE_RESULT.NO_UPDATE && !mIsHomeCheck) {
                        ToastUtil.showToast(mContext, R.string.update_already_new, Toast.LENGTH_SHORT);
                    } else if (updateMsg.eResult == AppUpdateService.CHECK_UPDATE_RESULT.TIMEOUT && !mIsHomeCheck) {
                        ToastUtil.showToast(mContext, R.string.update_check_timeout, Toast.LENGTH_SHORT);
                    } else if (updateMsg.eResult == AppUpdateService.CHECK_UPDATE_RESULT.EXCEPTION && !mIsHomeCheck) {
                        ToastUtil.showToast(mContext, R.string.update_check_exception, Toast.LENGTH_SHORT);
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
