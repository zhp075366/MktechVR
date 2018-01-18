package com.mktech.smarthome.event;

import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/23
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomeMessageEvent<T> {

    private List<T> mData;
    private EVENT_TYPE mEventType = EVENT_TYPE.INVALID;

    public enum EVENT_TYPE {
        INVALID, TYPE_FIRST_LOAD_DATA
    }

    public HomeMessageEvent(EVENT_TYPE type) {
        mEventType = type;
    }

    public EVENT_TYPE getEventType() {
        return mEventType;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }
}
