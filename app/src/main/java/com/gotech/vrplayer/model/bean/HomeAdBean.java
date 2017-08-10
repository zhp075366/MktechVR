package com.gotech.vrplayer.model.bean;

/**
 * Author: ZouHaiping on 2017/6/21
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomeAdBean {
    private String url;
    private String reserve;

    public HomeAdBean(String url, String reserve) {
        this.url = url;
        this.reserve = reserve;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}
