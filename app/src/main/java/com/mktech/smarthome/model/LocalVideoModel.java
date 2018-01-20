package com.mktech.smarthome.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.mktech.smarthome.model.bean.LocalVideoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/11
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoModel {

    private Context mContext;

    public LocalVideoModel(Context context) {
        mContext = context;
    }

    public List<LocalVideoBean> getLocalVideoData() {
        List<LocalVideoBean> videoList = new ArrayList<>();
        String[] mediaColumns = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE};
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null, null);
        if (cursor == null) {
            return videoList;
        }
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            LocalVideoBean bean = new LocalVideoBean();
            bean.setPath(path);
            bean.setTitle(title);
            bean.setDisplayName(displayName);
            bean.setDuration(duration);
            bean.setSize(size);
            videoList.add(bean);
        }
        cursor.close();
        return videoList;
    }

    public Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
