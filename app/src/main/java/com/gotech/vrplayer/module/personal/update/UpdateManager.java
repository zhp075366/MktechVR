package com.gotech.vrplayer.module.personal.update;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.TextView;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.AppUtil;
import com.socks.library.KLog;

public class UpdateManager implements OnClickListener {

    private Context mContext;
    private Dialog mDialog;
    private Button mBtnOK;
    private Button mBtnCancel;
    private TextView mTextTitle;
    private TextView mTextContent;
    private LayoutInflater mInflater;
    private UpdateService mUpdateService;

    private int mAppSize;
    private String mAppMD5;

    public UpdateManager(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @SuppressLint("InflateParams")
    public void initDialogView() {
        View dialogView = mInflater.inflate(R.layout.dialog_update_tip, null);
        mDialog = DialogCreater.showDownloadDialog(mContext, R.style.UpdateDialog, dialogView);
        mBtnOK = (Button)dialogView.findViewById(R.id.okButton);
        mBtnCancel = (Button)dialogView.findViewById(R.id.cancelButton);
        mTextTitle = (TextView)dialogView.findViewById(R.id.title);
        mTextContent = (TextView)dialogView.findViewById(R.id.text_content);
    }

    public void showUpdateDialog(String updateInfo) {
        mTextContent.setText(updateInfo);
        mBtnOK.setText(R.string.update_btnupdate);
        mBtnCancel.setText(R.string.update_btnnext);
        mTextTitle.setText(R.string.update_title);
        mBtnOK.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mDialog.show();
    }

    public void saveAppInfo(String appMd5, int fileLength) {
        mAppMD5 = appMd5;
        mAppSize = fileLength;
    }

    public UpdateService.UPDATE_SERVICE_STATE getServiceState() {
        return mUpdateService.getServiceState();
    }

    public void setUIHandler(Handler handler) {
        mUpdateService.setUIHandler(handler);
    }

    public void checkUpdate() {
        mUpdateService.startCheckUpdate();
    }

    private ServiceConnection onUpdateServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            KLog.i("updateService bind ok");
            mUpdateService = ((UpdateService.UpdateServiceBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            KLog.e("updateService bind error");
            mUpdateService = null;
        }
    };

    public void startUpdateService() {
        if (AppUtil.isServiceRunning(mContext, UpdateService.class.getName())) {
            KLog.e("updateService is running");
            return;
        }
        KLog.e("updateService is not running");
        Intent intent = new Intent(mContext, UpdateService.class);
        KLog.i("start updateService");
        mContext.startService(intent);
    }

    public void stopUpdateServiceIfStarted() {
        if (!AppUtil.isServiceRunning(mContext, UpdateService.class.getName())) {
            KLog.e("updateService is not running");
            return;
        }
        KLog.e("updateService is running");
        Intent intent = new Intent(mContext, UpdateService.class);
        KLog.i("stop updateService");
        mContext.stopService(intent);
    }

    public void unbindUpdateService() {
        mContext.unbindService(onUpdateServiceConnection);
        mUpdateService = null;
    }

    public void bindUpdateService() {
        KLog.i("bind updateService");
        Intent intentBackupService = new Intent(mContext, UpdateService.class);
        mContext.bindService(intentBackupService, onUpdateServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.okButton:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    if (mUpdateService != null) {
                        mUpdateService.initNotification();
                        mUpdateService.startDownloadApp(mAppMD5, mAppSize);
                    }
                }
                break;
            case R.id.cancelButton:
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                    if (mUpdateService != null) {
                        mUpdateService.setServiceState(UpdateService.UPDATE_SERVICE_STATE.IDLE);
                    }
                }
                break;
        }
    }
}
