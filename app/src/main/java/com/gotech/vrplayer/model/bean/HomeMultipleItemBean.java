package com.gotech.vrplayer.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class HomeMultipleItemBean implements MultiItemEntity {

    public static final int TITLE = 1;
    public static final int PICTURE = 2;
    public static final int WIDE_PICTURE = 3;

    public static final int TITLE_SPAN_SIZE = 2;
    public static final int PICTURE_SPAN_SIZE = 1;
    public static final int WIDE_PICTURE_SPAN_SIZE = 2;

    private int mItemType;
    private int mSpanSize;
    private String mTitle;
    private HomePictureBean mPicture;

    public HomeMultipleItemBean(int itemType, int spanSize, String title) {
        mItemType = itemType;
        mSpanSize = spanSize;
        mTitle = title;
    }

    public HomeMultipleItemBean(int itemType, int spanSize, HomePictureBean picture) {
        mItemType = itemType;
        mSpanSize = spanSize;
        mPicture = picture;
    }

    public int getSpanSize() {
        return mSpanSize;
    }

    public void setSpanSize(int spanSize) {
        mSpanSize = spanSize;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public HomePictureBean getPicture() {
        return mPicture;
    }

    public void setPicture(HomePictureBean picture) {
        mPicture = picture;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }
}
