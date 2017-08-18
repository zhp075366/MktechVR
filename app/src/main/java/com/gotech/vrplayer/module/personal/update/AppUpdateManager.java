package com.gotech.vrplayer.module.personal.update;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.AppUtil;
import com.gotech.vrplayer.widget.CustomDialog;
import com.socks.library.KLog;

public class AppUpdateManager implements OnClickListener {

    private Context mContext;
    private CustomDialog mDialog;
    private LayoutInflater mInflater;
    private AppUpdateService mAppUpdateService;

    private int mAppSize;
    private String mAppMD5;

    public AppUpdateManager(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @SuppressLint("InflateParams")
    public void showUpdateDialog(String updateInfo) {
        View dialogView = mInflater.inflate(R.layout.dialog_update_tip, null);
        mDialog = DialogCreater.showDownloadDialog(mContext, dialogView, updateInfo);
        Button btnOK = (Button)dialogView.findViewById(R.id.okButton);
        Button btnCancel = (Button)dialogView.findViewById(R.id.cancelButton);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        mDialog.show();
    }

    public void saveAppInfo(String appMd5, int fileLength) {
        mAppMD5 = appMd5;
        mAppSize = fileLength;
    }

    public AppUpdateService.UPDATE_SERVICE_STATE getServiceState() {
        return mAppUpdateService.getServiceState();
    }

    public void setUIHandler(Handler handler) {
        mAppUpdateService.setUIHandler(handler);
    }

    public void checkUpdate() {
        mAppUpdateService.startCheckUpdate();
    }

    private ServiceConnection onUpdateServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            KLog.i("updateService bind ok");
            mAppUpdateService = ((AppUpdateService.UpdateServiceBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            KLog.e("updateService bind error");
            mAppUpdateService = null;
        }
    };

    public void startUpdateService() {
        if (AppUtil.isServiceRunning(mContext, AppUpdateService.class.getName())) {
            KLog.e("updateService is running");
            return;
        }
        KLog.e("updateService is not running");
        Intent intent = new Intent(mContext, AppUpdateService.class);
        KLog.i("start updateService");
        mContext.startService(intent);
    }

    public void stopUpdateService() {
        if (!AppUtil.isServiceRunning(mContext, AppUpdateService.class.getName())) {
            KLog.e("updateService is not running");
            return;
        }
        KLog.e("updateService is running");
        Intent intent = new Intent(mContext, AppUpdateService.class);
        KLog.i("stop updateService");
        mContext.stopService(intent);
    }

    public void unbindUpdateService() {
        KLog.i("unbindUpdateService");
        mContext.unbindService(onUpdateServiceConnection);
        mAppUpdateService = null;
    }

    public void bindUpdateService() {
        KLog.i("bind updateService");
        Intent intentBackupService = new Intent(mContext, AppUpdateService.class);
        mContext.bindService(intentBackupService, onUpdateServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.okButton:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    if (mAppUpdateService != null) {
                        mAppUpdateService.initNotification();
                        mAppUpdateService.startDownloadApp(mAppMD5, mAppSize);
                    }
                }
                break;
            case R.id.cancelButton:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    if (mAppUpdateService != null) {
                        mAppUpdateService.setServiceState(AppUpdateService.UPDATE_SERVICE_STATE.IDLE);
                    }
                }
                break;
        }
    }
}
