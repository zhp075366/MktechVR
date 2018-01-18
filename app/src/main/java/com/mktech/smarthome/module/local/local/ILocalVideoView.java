package com.mktech.smarthome.module.local.local;

import android.graphics.Bitmap;

import com.mktech.smarthome.base.IBaseView;
import com.mktech.smarthome.model.bean.LocalVideoBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/8/10
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface ILocalVideoView extends IBaseView {

    void showLocalVideo(List<LocalVideoBean> data);

    void setThumbnail(int position, Bitmap bitmap);
}
