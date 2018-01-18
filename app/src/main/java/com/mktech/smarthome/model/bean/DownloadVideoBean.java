package com.mktech.smarthome.model.bean;

import java.io.Serializable;

/**
 * Author: ZouHaiping on 2017/7/25
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class DownloadVideoBean implements Serializable {

    private static final long serialVersionUID = 3755998332410346904L;

    public String downUrl; // 下载url
    public String iconUrl; // 图标url

    public String showName; // UI上显示的名字
    public String saveName; // 自定义的保存文件名

    public long totalSize;  // 下载文件的总大小

}
