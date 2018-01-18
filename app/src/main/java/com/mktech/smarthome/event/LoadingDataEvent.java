package com.mktech.smarthome.event;

import com.mktech.smarthome.model.bean.HomePictureBean;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/23
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LoadingDataEvent {

    private String mEventCode;
    private List<HomePictureBean> mData;
    private EVENT_TYPE mEventType = EVENT_TYPE.INVALID;

    public static final String VIDEO_DETAIL_EVENT_CODE = "VideoDetail";

    public enum EVENT_TYPE {
        INVALID, TYPE_FIRST_LOAD_DATA, TYPE_LOAD_MORE_DATA
    }

    public LoadingDataEvent(EVENT_TYPE type) {
        mEventType = type;
    }

    public EVENT_TYPE getEventType() {
        return mEventType;
    }

    public void setEventCode(String code) {
        mEventCode = code;
    }

    public String getEventCode() {
        return mEventCode;
    }

    public List<HomePictureBean> getData() {
        return mData;
    }

    public void setData(List<HomePictureBean> data) {
        mData = data;
    }
}
