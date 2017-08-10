package com.gotech.vrplayer.model.bean;

/**
 * 作者：Created by ZouHaiping on 2017/6/7 0007
 * 邮箱：haiping.zou@gotechcn.cn
 * 公司：MKTech
 */
public class HomeCategoryBean {

    private String type;
    private int imageId;

    public HomeCategoryBean(String type, int imageId) {
        this.type = type;
        this.imageId = imageId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return this.imageId;
    }
}
