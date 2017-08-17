package com.gotech.vrplayer.module.personal.update;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.gotech.vrplayer.R;
import com.gotech.vrplayer.utils.Constants;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class CheckUpdateThread extends Thread {
    private String mUrl;
    private Context mContext;
    private UpdateCallback mUpdateListener;

    public enum CHECK_UPDATE_RESULT {
        TIMEOUT, EXCEPTION, NO_UPDATE, HAVE_UPDATE
    }

    public class CheckUpdateMsg {
        public int appSize;
        public String strAppMd5;
        public String strCheckResult;
        public CHECK_UPDATE_RESULT eResult;
    }

    public CheckUpdateThread(String url, Context context, UpdateCallback listener) {
        mUrl = url;
        mContext = context;
        mUpdateListener = listener;
    }

    @Override
    public void run() {
        mUpdateListener.onCheckUpdateStart();
        CheckUpdateMsg resultMsg = new CheckUpdateMsg();
        resultMsg.strCheckResult = null;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            int nCurVersionCode = packageInfo.versionCode;
            String updateDetail = getUpdateDetail(mUrl, "UTF-8");
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
        mUpdateListener.onCheckUpdateComplete(resultMsg);
    }

    private String getUpdateDetail(String requestUrl, String charset) throws Exception {
        String detailInfo = "";
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(60000);
            urlConnection.setConnectTimeout(10000);
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
}
