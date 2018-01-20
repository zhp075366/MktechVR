package com.mktech.smarthome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mktech.smarthome.SmartHomeApplication;

/**
 * SharedPrefernce操作工具类
 */
public class SharedPreferenceUtil {
    public static String getString(String name, String key, String _default){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, _default);
    }

    public static int getInt(String name, String key, int _default){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, _default);
    }

    public static boolean getBoolean(String name, String key, boolean _default){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, _default);
    }

    public static void putString(String name, String key, String value){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void putInt(String name, String key, int value){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static void putBoolean(String name, String key, boolean value){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void remove(String name, String key){
        SharedPreferences sp = SmartHomeApplication.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
}
