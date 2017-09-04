package com.gotech.vrplayer.module.personal.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.AsyncTaskWrapper;
import com.gotech.vrplayer.utils.Constants;
import com.gotech.vrplayer.utils.MD5Util;
import com.gotech.vrplayer.utils.SDCardUtil;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class AppUpdateService extends Service {

    private Context mContext;
    private Resources mResources;
    private volatile Handler mUIHandler;
    private UPDATE_SERVICE_STATE mServiceState;
    private UpdateServiceBinder mUpdateServiceBinder = new UpdateServiceBinder();

    // Hander消息
    private static final int MESSAGE_ID_START = 0x00000000;
    public static final int AUTO_UPDATE_CHECKING_COMPLETE = MESSAGE_ID_START + 1;
    public static final int AUTO_UPDATE_DOWNLOADING_COMPLETE = AUTO_UPDATE_CHECKING_COMPLETE + 1;

    // 检测更新消息
    enum CHECK_UPDATE_RESULT {
        TIMEOUT, EXCEPTION, NO_UPDATE, HAVE_UPDATE
    }

    class CheckUpdateMsg {
        int appSize;
        String strAppMd5;
        String strCheckResult;
        CHECK_UPDATE_RESULT eResult;
    }

    // 下载更新消息
    private enum DOWNLOAD_UPDATE_RESULT {
        FAIL, SUCCESS
    }

    class DownloadUpdateMsg {
        String strDownloadResult;
        DOWNLOAD_UPDATE_RESULT eResult;
    }

    // 服务器相关信息
    private static final String UPDATE_CFG_FILE = "update.cfg";
    private static final String DOWNLOAD_APK_FILE = "MKIPC.apk";
    private static final String SERVER_ROOT = "http://rdtest.myhiott.com:8081/ftp_test/lifeiheng/ipc_android_apk/";

    // 通知
    private Notification.Builder mNBuilder;
    private NotificationManager mNotificationManager;

    // 检测更新任务
    private AsyncTaskWrapper<Void, Void, CheckUpdateMsg> mCheckUpdateTask;
    private AsyncTaskWrapper.OnLoadListener<Void, Void, CheckUpdateMsg> mCheckUpdateListener;
    // 下载更新任务
    private AsyncTaskWrapper<Void, Integer, DownloadUpdateMsg> mDownloadUpdateTask;
    private AsyncTaskWrapper.OnLoadListener<Void, Integer, DownloadUpdateMsg> mDownloadUpdateListener;

    // 下载APK相关信息
    private int mAPKSize;
    private String mAPKMD5;
    private int mReceivedBytes;

    // Service状态获取锁
    private final Object mLock = new Object();

    enum UPDATE_SERVICE_STATE {
        IDLE, CHECKING, DOWNLOADINIG, DESTROY
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = AppUpdateService.this;
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
        setServiceState(UPDATE_SERVICE_STATE.IDLE);
        initCheckUpdateListener();
        initDownloadUpdateListener();
        return super.onStartCommand(intent, flags, startId);
    }

    public void setUIHandler(Handler handler) {
        mUIHandler = handler;
    }

    public void startDownloadUpdate(String appMd5, int fileLength) {
        mAPKMD5 = appMd5;
        mAPKSize = fileLength;
        mDownloadUpdateTask = new AsyncTaskWrapper<>();
        mDownloadUpdateTask.setTaskTag("DownloadUpdateTask");
        mDownloadUpdateTask.setOnTaskListener(mDownloadUpdateListener);
        mDownloadUpdateTask.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_CACHED);
    }

    public void startCheckUpdate() {
        mCheckUpdateTask = new AsyncTaskWrapper<>();
        mCheckUpdateTask.setTaskTag("CheckUpdate");
        mCheckUpdateTask.setOnTaskListener(mCheckUpdateListener);
        mCheckUpdateTask.executeOnExecutor(AsyncTaskWrapper.THREAD_POOL_CACHED);
    }

    public UPDATE_SERVICE_STATE getServiceState() {
        synchronized (mLock) {
            return mServiceState;
        }
    }

    public void setServiceState(UPDATE_SERVICE_STATE eState) {
        synchronized (mLock) {
            mServiceState = eState;
        }
    }

    class UpdateServiceBinder extends Binder {
        AppUpdateService getService() {
            return AppUpdateService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.e("updateService onDestroy");
    }

    private void installPackage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String saveDir = SDCardUtil.getDownloadSaveRootPath();
        File file = new File(saveDir, DOWNLOAD_APK_FILE);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        mContext.startActivity(intent);
    }

    private void sendCheckResultMessage(int what, CheckUpdateMsg checkMsg) {
        if (mUIHandler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = checkMsg;
            mUIHandler.sendMessage(msg);
        }
    }

    private void sendDownloadResultMessage(int what, DownloadUpdateMsg downloadMsg) {
        if (mUIHandler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = downloadMsg;
            mUIHandler.sendMessage(msg);
        }
    }

    public void initNotification() {
        Intent intent = new Intent();
        PendingIntent intentContent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNBuilder = new Notification.Builder(mContext);
        mNBuilder.setOngoing(true);
        mNBuilder.setAutoCancel(false);
        mNBuilder.setContentIntent(intentContent);
        mNBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNBuilder.setTicker(mResources.getString(R.string.notification_title));
        mNBuilder.setContentTitle(mResources.getString(R.string.notification_title));
        notifyDownloadProgress(0);
    }

    @SuppressWarnings("deprecation")
    private void notifyDownloadProgress(int progress) {
        Notification notification;
        mNBuilder.setContentText(progress + "%");
        mNBuilder.setProgress(100, progress, false);
        if (Build.VERSION.SDK_INT >= 16) {
            notification = mNBuilder.build();
        } else {
            notification = mNBuilder.getNotification();
        }
        mNotificationManager.notify(0, notification);
    }

    private void initCheckUpdateListener() {
        mCheckUpdateListener = new AsyncTaskWrapper.OnLoadListener<Void, Void, CheckUpdateMsg>() {
            @Override
            public void onStart(Object taskTag) {
                setServiceState(UPDATE_SERVICE_STATE.CHECKING);
            }

            @Override
            public void onResult(Object taskTag, CheckUpdateMsg checkUpdateMsg) {
                if (checkUpdateMsg.eResult != CHECK_UPDATE_RESULT.HAVE_UPDATE) {
                    setServiceState(UPDATE_SERVICE_STATE.IDLE);
                }
                sendCheckResultMessage(AUTO_UPDATE_CHECKING_COMPLETE, checkUpdateMsg);
            }

            @Override
            public CheckUpdateMsg onWorkerThread(Object taskTag, Void... params) {
                return checkUpdateRun();
            }
        };
    }

    private void initDownloadUpdateListener() {
        mDownloadUpdateListener = new AsyncTaskWrapper.OnLoadListener<Void, Integer, DownloadUpdateMsg>() {
            @Override
            public void onStart(Object taskTag) {
                setServiceState(UPDATE_SERVICE_STATE.DOWNLOADINIG);
            }

            @Override
            public void onCancel(Object taskTag) {
                setServiceState(UPDATE_SERVICE_STATE.IDLE);
            }

            @Override
            public void onResult(Object taskTag, DownloadUpdateMsg downloadUpdateMsg) {
                mNotificationManager.cancel(0);
                if (downloadUpdateMsg.eResult == DOWNLOAD_UPDATE_RESULT.FAIL) {
                    downloadUpdateMsg.strDownloadResult = mResources.getString(R.string.update_failed_msg);
                    sendDownloadResultMessage(AUTO_UPDATE_DOWNLOADING_COMPLETE, downloadUpdateMsg);
                } else if (downloadUpdateMsg.eResult == DOWNLOAD_UPDATE_RESULT.SUCCESS) {
                    downloadUpdateMsg.strDownloadResult = mResources.getString(R.string.update_success_msg);
                    sendDownloadResultMessage(AUTO_UPDATE_DOWNLOADING_COMPLETE, downloadUpdateMsg);
                    installPackage();
                }
                setServiceState(UPDATE_SERVICE_STATE.IDLE);
            }

            @Override
            public void onProgress(Object taskTag, Integer... values) {
                KLog.i("progress=" + values[0]);
                notifyDownloadProgress(values[0]);
            }

            @Override
            public DownloadUpdateMsg onWorkerThread(Object taskTag, Void... params) {
                return downloadUpdateRun();
            }
        };
    }

    public CheckUpdateMsg checkUpdateRun() {
        CheckUpdateMsg resultMsg = new CheckUpdateMsg();
        resultMsg.strCheckResult = null;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            int nCurVersionCode = packageInfo.versionCode;
            String updateDetail = getUpdateConfig();
            JSONArray arr = new JSONArray(updateDetail);
            if (arr.length() > 0) {
                JSONObject obj = arr.getJSONObject(0);
                String strMd5 = obj.getString("appMD5");
                int appSize = obj.getInt("appSize");
                int nNewVersionCode = obj.getInt("verCode");
                String updateVersion = mContext.getResources().getString(R.string.update_info_version);
                String updateSize = mContext.getResources().getString(R.string.update_info_size);
                String updateContent = mContext.getResources().getString(R.string.update_info_content);
                String sizeStr = Constants.TWO_DECIMAL_FORMAT.format((double)appSize / 1024 / 1024);
                String strTipsContent = String.format(updateSize, sizeStr) + "\n";
                strTipsContent = strTipsContent + updateVersion + obj.getString("verName") + "\n";
                strTipsContent = strTipsContent + updateContent + obj.getString("updateInfo");
                if (nNewVersionCode > nCurVersionCode) {
                    resultMsg.appSize = appSize;
                    resultMsg.strAppMd5 = strMd5;
                    resultMsg.strCheckResult = strTipsContent;
                    resultMsg.eResult = CHECK_UPDATE_RESULT.HAVE_UPDATE;
                } else {
                    resultMsg.eResult = CHECK_UPDATE_RESULT.NO_UPDATE;
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            resultMsg.eResult = CHECK_UPDATE_RESULT.TIMEOUT;
            KLog.e("CheckUpdate ConnectTimeoutException");
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg.eResult = CHECK_UPDATE_RESULT.EXCEPTION;
            KLog.e("CheckUpdate OtherException");
        }
        return resultMsg;
    }

    private String getUpdateConfig() throws Exception {
        String detailInfo = "";
        String charset = "UTF-8";
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(SERVER_ROOT + UPDATE_CFG_FILE);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "text/plain;charset=utf-8");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset));
                StringBuilder stringBuilder = new StringBuilder("");
                String line;
                String NL = System.getProperty("line.separator");
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(NL);
                }
                detailInfo = stringBuilder.toString();
                KLog.i(detailInfo);
            } else {
                KLog.e("ResponseCode:" + responseCode + ", msg:" + urlConnection.getResponseMessage());
                throw new Exception();
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    KLog.e(e.getMessage());
                }
            }
        }
        return detailInfo;
    }

    private boolean createDownloadFile() {
        String saveDir = SDCardUtil.getDownloadSaveRootPath();
        File file = new File(saveDir, DOWNLOAD_APK_FILE);
        if (file.exists()) {
            file.delete();
        }
        SDCardUtil.createFolder(saveDir);
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rwd");
            out.setLength(mAPKSize);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            KLog.e("createDownloadFile error");
            return false;
        }
        return true;
    }

    private DownloadUpdateMsg checkApkFileMD5(boolean bSuccess) {
        DownloadUpdateMsg resultMsg = new DownloadUpdateMsg();
        if (bSuccess) {
            String saveDir = SDCardUtil.getDownloadSaveRootPath();
            File file = new File(saveDir, DOWNLOAD_APK_FILE);
            String strDownloadedMd5 = MD5Util.getFileMD5(file);
            if (mAPKMD5.equals(strDownloadedMd5)) {
                resultMsg.eResult = DOWNLOAD_UPDATE_RESULT.SUCCESS;
            } else {
                KLog.e("DownloadUpdate MD5 Not Equal");
                resultMsg.eResult = DOWNLOAD_UPDATE_RESULT.FAIL;
            }
        } else {
            resultMsg.eResult = DOWNLOAD_UPDATE_RESULT.FAIL;
        }
        return resultMsg;
    }

    private DownloadUpdateMsg downloadUpdateRun() {
        if (!createDownloadFile()) {
            DownloadUpdateMsg resultMsg = new DownloadUpdateMsg();
            resultMsg.eResult = DOWNLOAD_UPDATE_RESULT.FAIL;
            return resultMsg;
        }
        mReceivedBytes = 0;
        int retryCount = 10;
        boolean bSuccess = false;
        for (int i = 0; i < retryCount; i++) {
            bSuccess = downloadAppFile();
            if (bSuccess) {
                break;
            }
        }
        return checkApkFileMD5(bSuccess);
    }

    private boolean downloadAppFile() {
        int progress;
        int currentProgress = 0;
        RandomAccessFile raFile = null;
        BufferedInputStream in = null;
        boolean bDownloadComplete = false;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(SERVER_ROOT + DOWNLOAD_APK_FILE);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Range", "bytes=" + mReceivedBytes + "-" + (mAPKSize - 1));
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            urlConnection.connect();
            int nResponseCode = urlConnection.getResponseCode();
            if (nResponseCode == HttpURLConnection.HTTP_OK || nResponseCode == HttpURLConnection.HTTP_PARTIAL) {
                String saveDir = SDCardUtil.getDownloadSaveRootPath();
                File file = new File(saveDir, DOWNLOAD_APK_FILE);
                raFile = new RandomAccessFile(file, "rwd");
                raFile.seek(mReceivedBytes);
                byte[] buffer = new byte[10240];
                in = new BufferedInputStream(urlConnection.getInputStream());
                do {
                    int nRead = in.read(buffer);
                    if (nRead > 0) {
                        raFile.write(buffer, 0, nRead);
                        mReceivedBytes += nRead;
                    }
                    progress = (int)(((float)mReceivedBytes / mAPKSize) * 100);
                    if (progress - currentProgress >= 1) {
                        currentProgress = progress;
                        mDownloadUpdateTask.updateProgress(progress);
                    }
                    if (nRead < 0 && mReceivedBytes == mAPKSize) {
                        bDownloadComplete = true;
                        break;
                    }
                } while (true);
            } else {
                KLog.e("ResponseCode:" + nResponseCode + ", msg:" + urlConnection.getResponseMessage() + " retry...");
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            KLog.e("DownloadUpdate SocketTimeoutException retry");
        } catch (IOException e) {
            e.printStackTrace();
            KLog.e("DownloadUpdate IOException retry");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raFile != null) {
                try {
                    raFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bDownloadComplete;
    }
}