package com.gotech.vrplayer.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.gotech.vrplayer.VRApplication;

import java.io.File;

/**
 * Author: ZouHaiping on 2017/7/28
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: SDCard操作的工具类
 */
public class SDCardUtil {

    private static final String DOWNLOAD_DIR = "Download";
    public static final String DOWNLOAD_APK_NAME = "MKIPC.apk";

    public static String getDownloadSaveRootPath() {
        String packageName = VRApplication.getApplication().getPackageName();
        String downloadDir = packageName + File.separator + DOWNLOAD_DIR;
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + downloadDir;
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param folderPath folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createFolder(String folderPath) {
        if (!TextUtils.isEmpty(folderPath)) {
            File folder = new File(folderPath);
            return createFolder(folder);
        }
        return false;
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param targetFolder folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) {
                return true;
            }
            targetFolder.delete();
        }
        return targetFolder.mkdirs();
    }
}
