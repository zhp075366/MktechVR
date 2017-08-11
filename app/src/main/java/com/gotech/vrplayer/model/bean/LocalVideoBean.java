package com.gotech.vrplayer.model.bean;

/**
 * Author: ZouHaiping on 2017/8/11
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class LocalVideoBean {

    private String title;
    private String displayName;
    private String path;
    private long size;
    private long duration;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
