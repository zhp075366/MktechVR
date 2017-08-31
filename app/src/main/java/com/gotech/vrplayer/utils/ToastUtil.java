package com.gotech.vrplayer.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.gotech.vrplayer.VRApplication;

public class ToastUtil {

    private static Toast mToast;

    // 以资源ID的方式显示
    public static void showToast(int resID) {
        toast(getStringRes(resID), Toast.LENGTH_SHORT);
    }

    public static void showToast(int resID, int time) {
        toast(getStringRes(resID), time);
    }

    // 直接传内容方式显示
    public static void showToast(String content) {
        toast(content, Toast.LENGTH_SHORT);
    }

    public static void showToast(String content, int time) {
        toast(content, time);
    }

    // 取消一个Toast
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    // 拿到字串资源
    @NonNull
    private static String getStringRes(int resID) {
        Context context = VRApplication.getApplication();
        return context.getResources().getString(resID);
    }

    // 取消上一条Toast
    private static void toast(String content, int time) {
        cancelToast();
        Context context = VRApplication.getApplication();
        mToast = Toast.makeText(context, content, time);
        mToast.show();
    }
}
