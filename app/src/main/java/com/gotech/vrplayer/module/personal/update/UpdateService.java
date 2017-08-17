package com.gotech.vrplayer.module.personal.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.SDCardUtil;
import com.socks.library.KLog;

import java.io.File;

public class UpdateService extends Service {

    private Handler mHandler;
    private Context mContext;
    private Resources mResources;
    private CheckUpdateThread mCheckThread;
    private DownloadUpdateThread mDownloadThread;
    private UPDATE_SERVICE_STATE mServiceState = UPDATE_SERVICE_STATE.IDLE;
    private UpdateServiceBinder mUpdateServiceBinder = new UpdateServiceBinder();

    // 更新服务器信息
    private static final String UPDATE_CFG = "update.cfg";
    private static final String UPDATE_FILE = "MKIPC.apk";
    private static final String SERVER_ROOT = "http://192.168.10.32/ipc_update/";

    // 通知
    private Notification m_Notification;
    private Notification.Builder m_NBuilder;
    private NotificationManager m_NotificationManager;

    public enum UPDATE_SERVICE_STATE {
        IDLE, CHECKING, DOWNLOADINIG
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = UpdateService.this;
        mResources = mContext.getResources();
        KLog.i("updateService onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mUpdateServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.i("updateService onStart");
        mServiceState = UPDATE_SERVICE_STATE.IDLE;
        return super.onStartCommand(intent, flags, startId);
    }

    public void setExternalHandler(Handler handler) {
        mHandler = handler;
    }

    public void startDownloadApp(String appMd5, int fileLength) {
        mDownloadThread = new DownloadUpdateThread(SERVER_ROOT + UPDATE_FILE, mUpdateListener, appMd5, fileLength);
        mDownloadThread.setTimeoutCount(10);
        mDownloadThread.start();
    }

    public void startCheckUpdate() {
        mCheckThread = new CheckUpdateThread(SERVER_ROOT + UPDATE_CFG, mContext, mUpdateListener);
        mCheckThread.start();
    }

    public UPDATE_SERVICE_STATE getServiceState() {
        return mServiceState;
    }

    public void setServiceState(UPDATE_SERVICE_STATE eState) {
        mServiceState = eState;
    }

    public class UpdateServiceBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.e("updateService onDestroy");
        if (m_NotificationManager != null)
            m_NotificationManager.cancel(0);
        try {
            if (mDownloadThread != null && mDownloadThread.isAlive()) {
                mDownloadThread.mStop = true;
                mDownloadThread.join();
                mDownloadThread = null;
            }
            if (mCheckThread != null && mCheckThread.isAlive()) {
                mCheckThread.join();
                mCheckThread = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void installPackage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(SDCardUtil.getDownloadSaveRootPath(), SDCardUtil.DOWNLOAD_APK_NAME);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        mContext.startActivity(intent);
    }

    private void sendCheckResultMessage(int what, CheckUpdateThread.CheckUpdateMsg checkMsg) {
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = checkMsg;
            mHandler.sendMessage(msg);
        }
    }

    private void sendDownloadResultMessage(int what, DownloadUpdateThread.DownloadUpdateMsg downloadMsg) {
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = downloadMsg;
            mHandler.sendMessage(msg);
        }
    }

    public void initNotification() {
        Intent intent = new Intent();
        PendingIntent intentContent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        m_NotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 11) {
            m_NBuilder = new Notification.Builder(mContext);
            m_NBuilder.setOngoing(true);
            m_NBuilder.setAutoCancel(false);
            m_NBuilder.setContentIntent(intentContent);
            m_NBuilder.setSmallIcon(R.mipmap.ic_launcher);
            m_NBuilder.setTicker(mResources.getString(R.string.notification_title));
            m_NBuilder.setContentTitle(mResources.getString(R.string.notification_title));
        } else {
            m_Notification = new Notification();
            m_Notification.icon = R.mipmap.ic_download;
            m_Notification.tickerText = mResources.getString(R.string.notification_title);
            m_Notification.contentIntent = intentContent;
        }
        this.notifyDownloadProgress(0);
    }

    @SuppressWarnings("deprecation")
    private void notifyDownloadProgress(int progress) {
        if (Build.VERSION.SDK_INT >= 11) {
            m_NBuilder.setContentText(progress + "%");
            m_NBuilder.setProgress(100, progress, false);
            if (Build.VERSION.SDK_INT >= 16) {
                m_Notification = m_NBuilder.build();
            } else {
                m_Notification = m_NBuilder.getNotification();
            }
        } else {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_download_progressbar);
            remoteViews.setProgressBar(R.id.notificationProgress, 100, progress, false);
            remoteViews.setTextViewText(R.id.notificationPercent, progress + "%");
            m_Notification.contentView = remoteViews;
        }
        m_NotificationManager.notify(0, m_Notification);
    }

    private UpdateCallback mUpdateListener = new UpdateCallback() {

        @Override
        public void onCheckUpdateStart() {
            mServiceState = UPDATE_SERVICE_STATE.CHECKING;
        }

        @Override
        public void onDownloadUpdateStart() {
            mServiceState = UPDATE_SERVICE_STATE.DOWNLOADINIG;
        }

        @Override
        public void onDownloadUpdateProgress(int nProgress) {
            KLog.i("progress=" + nProgress);
            notifyDownloadProgress(nProgress);
        }

        @Override
        public void onCheckUpdateComplete(CheckUpdateThread.CheckUpdateMsg updateMsg) {
            mServiceState = UPDATE_SERVICE_STATE.IDLE;
            sendCheckResultMessage(MessageID.AUTO_UPDATE_CHECKING_COMPLETE, updateMsg);
        }

        @Override
        public void onDownloadUpdateFail() {
            KLog.e("onDownloadUpdateFail");
            m_NotificationManager.cancel(0);
            mServiceState = UPDATE_SERVICE_STATE.IDLE;
            DownloadUpdateThread.DownloadUpdateMsg downloadMsg = new DownloadUpdateThread.DownloadUpdateMsg();
            downloadMsg.strDownloadResult = mResources.getString(R.string.update_failed_msg);
            downloadMsg.eResult = DownloadUpdateThread.DOWNLOAD_UPDATE_RESULT.FAIL;
            sendDownloadResultMessage(MessageID.AUTO_UPDATE_DOWNLOADING_COMPLETE, downloadMsg);
        }

        @Override
        public void onDownloadUpdateSuccess() {
            KLog.i("onDownloadUpdateSuccess");
            m_NotificationManager.cancel(0);
            mServiceState = UPDATE_SERVICE_STATE.IDLE;
            DownloadUpdateThread.DownloadUpdateMsg downloadMsg = new DownloadUpdateThread.DownloadUpdateMsg();
            downloadMsg.strDownloadResult = mResources.getString(R.string.update_success_msg);
            downloadMsg.eResult = DownloadUpdateThread.DOWNLOAD_UPDATE_RESULT.SUCCESS;
            sendDownloadResultMessage(MessageID.AUTO_UPDATE_DOWNLOADING_COMPLETE, downloadMsg);
            installPackage();
        }
    };
}