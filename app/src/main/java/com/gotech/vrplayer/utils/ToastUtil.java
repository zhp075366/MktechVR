package com.gotech.vrplayer.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class ToastUtil {

    private static Toast mToast;

    // 以资源ID的方式显示
    public static void showToast(Context context, int resID, int time) {
        if (context != null) {
            toast(context.getApplicationContext(), getStringRes(context, resID), time);
        }
    }

    // 直接传内容方式显示
    public static void showToast(Context context, String content, int time) {
        if (context != null) {
            toast(context.getApplicationContext(), content, time);
        }
    }

    // 取消Toast
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    // 拿到字串资源
    @NonNull
    private static String getStringRes(Context context, int resID) {
        return context.getResources().getString(resID);
    }

    // 取消上一条Toast
    private static void toast(Context context, String content, int time) {
        cancelToast();
        if (context != null) {
            mToast = Toast.makeText(context, content, time);
            mToast.show();
        }
    }
}
