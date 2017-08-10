package com.gotech.vrplayer.utils;

import android.os.Environment;

import com.gotech.vrplayer.VRApplication;

import java.io.File;

/**
 * Author: ZouHaiping on 2017/7/28
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: SDCard操作的工具类
 */
public class SDCardUtil {

    private static final String DOWNLOAD_DIR = "Download";

    public static String getDownloadSaveRootPath() {
        String packageName = VRApplication.getApplication().getPackageName();
        String downloadDir = packageName + File.separator + DOWNLOAD_DIR;
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + downloadDir;
    }

}
