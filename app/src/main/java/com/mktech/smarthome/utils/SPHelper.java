package com.mktech.smarthome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: ZouHaiping on 2017/8/2
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class SPHelper {

    private static final String FILE_NAME = "spfile";
    private static final String SEARCH_HISTORY = "search_history";
    
    public static String getSearchHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(SEARCH_HISTORY, "");
    }

    public static void setSearchHistory(Context context, String history) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SEARCH_HISTORY, history);
        editor.apply();
    }
}
