package com.gotech.vrplayer.model.bean;

/**
 * Author: ZouHaiping on 2017/6/21
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomePictureBean {

    private String url;
    private String mainDescribe;
    private String subDescribe;

    public HomePictureBean(String url, String mainDes, String subDes) {
        this.url = url;
        this.mainDescribe = mainDes;
        this.subDescribe = subDes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainDescribe() {
        return mainDescribe;
    }

    public void setMainDescribe(String mainDes) {
        this.mainDescribe = mainDes;
    }

    public String getSubDescribe() {
        return subDescribe;
    }

    public void setSubDescribe(String subDes) {
        this.subDescribe = subDes;
    }
}
