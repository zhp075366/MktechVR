package com.mktech.smarthome.utils;

import android.content.Context;

import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SDKUtil {
    /**
     * 读取配置文件
     *
     * @param context app context
     * @throws IOException
     */
    public static void copyFile(Context context, String fileName) throws IOException {
        File filesDir = context.getFilesDir();
        File initFile = new File(filesDir, fileName);
        if (initFile.exists()) {
            KLog.i("test1.h264 file exists path=" + initFile.getAbsolutePath());
            return;
        }
        InputStream inputStream = context.getAssets().open(fileName);
        if (inputStream == null) {
            return;
        }
        byte[] buffer = new byte[1024];
        FileOutputStream fos = new FileOutputStream(initFile);
        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            fos.write(buffer, 0, read);
        }
        inputStream.close();
        fos.close();
    }
}
