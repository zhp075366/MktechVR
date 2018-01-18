package com.mktech.smarthome.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: ZouHaiping on 2017/7/4
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoChannelBean implements Parcelable {

    private String mTitle;
    private String mTitleCode;

    public VideoChannelBean(String title, String titleCode) {
        mTitle = title;
        mTitleCode = titleCode;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitleCode(String code) {
        mTitleCode = code;
    }

    public String getTitleCode() {
        return mTitleCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mTitleCode);
    }

    protected VideoChannelBean(Parcel in) {
        this.mTitle = in.readString();
        this.mTitleCode = in.readString();
    }

    public static final Parcelable.Creator<VideoChannelBean> CREATOR = new Parcelable.Creator<VideoChannelBean>() {
        @Override
        public VideoChannelBean createFromParcel(Parcel source) {
            return new VideoChannelBean(source);
        }

        @Override
        public VideoChannelBean[] newArray(int size) {
            return new VideoChannelBean[size];
        }
    };
}
