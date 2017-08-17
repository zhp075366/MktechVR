package com.gotech.vrplayer.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/16
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class AppUtil {

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        }
        catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定的Service是否在运行
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listService = activityManager.getRunningServices(100);
        if (listService.isEmpty()) {
            return false;
        }
        boolean bIsRunning = false;
        for (int i = 0; i < listService.size(); i++) {
            if (listService.get(i).service.getClassName().equals(className)) {
                bIsRunning = true;
                break;
            }
        }
        return bIsRunning;
    }

}
