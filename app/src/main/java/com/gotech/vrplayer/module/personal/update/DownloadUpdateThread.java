package com.gotech.vrplayer.module.personal.update;

import com.gotech.vrplayer.utils.MD5Util;
import com.gotech.vrplayer.utils.SDCardUtil;
import com.socks.library.KLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class DownloadUpdateThread extends Thread {

    private String mUrl;
    private String mStrUpdateMd5;
    private int mReceivedBytes;
    private int mTimeoutCount;
    private int mFileLentgh;
    private int mCurrentProgress;
    public boolean mStop = false;
    private UpdateCallback mUpdateListener;

    public enum DOWNLOAD_UPDATE_RESULT {
        FAIL, SUCCESS
    }

    public static class DownloadUpdateMsg {
        public String strDownloadResult;
        public DOWNLOAD_UPDATE_RESULT eResult;
    }

    public DownloadUpdateThread(String url, UpdateCallback listener, String appMd5, int fileLength) {
        mUrl = url;
        mFileLentgh = fileLength;
        mStrUpdateMd5 = appMd5;
        mUpdateListener = listener;
    }

    public void setTimeoutCount(int count) {
        mTimeoutCount = count;
    }

    private void createDownloadFile(File file, int fileLength) throws IOException {

    }

    @Override
    public void run() {
        boolean bRetry = false;
        boolean bDownloadComplete = false;
        mUpdateListener.onDownloadUpdateStart();
        String saveRoot = SDCardUtil.getDownloadSaveRootPath();
        File file = new File(saveRoot, SDCardUtil.DOWNLOAD_APK_NAME);
        if (file.exists()) {
            file.delete();
        }
        SDCardUtil.createFolder(saveRoot);
        try {
            RandomAccessFile out = new RandomAccessFile(file, "rwd");
            out.setLength(mFileLentgh);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            KLog.e("createDownloadFile error");
            mUpdateListener.onDownloadUpdateFail();
        }
        while (mTimeoutCount > 0) {
            if (bRetry) {
                bRetry = false;
                KLog.e("DownloadUpdate retry...");
            }
            int nProgress;
            RandomAccessFile raFile = null;
            BufferedInputStream in = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mUrl);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Range", "bytes=" + mReceivedBytes + "-" + (mFileLentgh - 1));
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                urlConnection.connect();
                int nResponseCode = urlConnection.getResponseCode();
                if (nResponseCode == HttpURLConnection.HTTP_OK || nResponseCode == HttpURLConnection.HTTP_PARTIAL) {
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
                        nProgress = (int)(((float)mReceivedBytes / mFileLentgh) * 100);
                        if (nProgress - mCurrentProgress >= 1) {
                            mCurrentProgress = nProgress;
                            mUpdateListener.onDownloadUpdateProgress(nProgress);
                        }
                        if (nRead < 0 && mReceivedBytes == mFileLentgh) {
                            bDownloadComplete = true;
                            break;
                        }
                    } while (!mStop);
                } else {
                    KLog.e("ResponseCode:" + nResponseCode + ", msg:" + urlConnection.getResponseMessage() + " retry...");
                }
            } catch (SocketTimeoutException e) {
                bRetry = true;
                mTimeoutCount--;
                e.printStackTrace();
                KLog.e("DownloadUpdate SocketTimeoutException retry");
            } catch (IOException e) {
                bRetry = true;
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
            if (bDownloadComplete || mStop) {
                break;
            }
        }
        if (bDownloadComplete) {
            String strDownloadedMd5 = MD5Util.getFileMD5(file);
            KLog.i("server MD5->" + mStrUpdateMd5);
            KLog.i("compute MD5->" + strDownloadedMd5);
            if (mStrUpdateMd5.equals(strDownloadedMd5)) {
                mUpdateListener.onDownloadUpdateSuccess();
            } else {
                KLog.e("DownloadUpdate Md5 Not Equal");
                mUpdateListener.onDownloadUpdateFail();
            }
        } else {
            mUpdateListener.onDownloadUpdateFail();
        }
    }
}