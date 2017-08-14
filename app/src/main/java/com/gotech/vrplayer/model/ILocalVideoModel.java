package com.gotech.vrplayer.model;

import android.graphics.Bitmap;

import com.gotech.vrplayer.model.bean.LocalVideoBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/11
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface ILocalVideoModel {

    List<LocalVideoBean> getLocalVideoData();

    Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind);
}
