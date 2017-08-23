package com.gotech.vrplayer.model.impl;

import com.gotech.vrplayer.model.IDownloadVideoModel;
import com.gotech.vrplayer.model.bean.DownloadVideoBean;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZouHaiping on 2017/7/26
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc: 只负债数据相关的操作，包括本地数据和数据库中数据
 */
public class DownloadVideoModelImpl implements IDownloadVideoModel {

    private DownloadManager mDownloadManager;

    public DownloadVideoModelImpl() {
        //数据库的操作类，主要是对Progress下载信息的 增，删，改，查
        mDownloadManager = DownloadManager.getInstance();
    }

    // 提供需要下载的数据
    @Override
    public List<DownloadVideoBean> getNeedDownloadTasks() {
        return initData();
    }

    // 提供数据库中下载完成的数据
    @Override
    public List<Progress> getFinishedProgress() {
        return mDownloadManager.getFinished();
    }

    // 提供数据库中正在下载的数据
    @Override
    public List<Progress> getDownloadingProgress() {
        return mDownloadManager.getDownloading();
    }

    private List<DownloadVideoBean> initData() {
        List<DownloadVideoBean> videos = new ArrayList<>();

        DownloadVideoBean apk1 = new DownloadVideoBean();
        apk1.showName = "QQ";
        apk1.saveName = "com.tencent.qq_save.apk";
        apk1.iconUrl = "";
        apk1.downUrl = "http://gdown.baidu.com/data/wisegame/74ac7c397e120549/QQ_708.apk";
        apk1.totalSize = 43566497;
        videos.add(apk1);

        DownloadVideoBean apk2 = new DownloadVideoBean();
        apk2.showName = "聚美优品";
        apk2.saveName = "com.jm.android.jumei_save.apk";
        apk2.iconUrl = "";
        apk2.downUrl = "http://gdown.baidu.com/data/wisegame/5998b64e9e606ccb/jumeiyoupin_4652.apk";
        apk2.totalSize = 50209651;
        videos.add(apk2);

        DownloadVideoBean apk3 = new DownloadVideoBean();
        apk3.showName = "酷狗音乐";
        apk3.saveName = "kugouyinle_save.apk";
        apk3.iconUrl = "";
        apk3.downUrl = "http://gdown.baidu.com/data/wisegame/ca9305e85a08b26d/kugouyinle_8851.apk";
        apk3.totalSize = 45741101;
        videos.add(apk3);

        DownloadVideoBean apk4 = new DownloadVideoBean();
        apk4.showName = "书旗小说";
        apk4.saveName = "com.shuqi.controller_save.apk";
        apk4.iconUrl = "";
        apk4.downUrl = "http://gdown.baidu.com/data/wisegame/7bea1eb26b14f294/shuqixiaoshuo_138.apk";
        apk4.totalSize = 28495747;
        videos.add(apk4);

        DownloadVideoBean apk5 = new DownloadVideoBean();
        apk5.showName = "有道云笔记";
        apk5.saveName = "youdaoyunbiji_save.apk";
        apk5.iconUrl = "";
        apk5.downUrl = "http://gdown.baidu.com/data/wisegame/bba90a8f9b03d5bd/youdaoyunbiji_77.apk";
        apk5.totalSize = 57624036;
        videos.add(apk5);

        //        DownloadVideoBean largeFile = new DownloadVideoBean();
        //        largeFile.showName = "androidstudio_2.3.0.0.exe";
        //        largeFile.saveName = "androidstudio_2.3.0.0_save.exe";
        //        largeFile.iconUrl = "";
        //        largeFile.downUrl = "http://sw.bos.baidu.com/sw-search-sp/software/5f5ecfa13d98c/androidstudio_2.3.0.0.exe";
        //        largeFile.totalSize = 1938754064;
        //        videos.add(largeFile);

        return videos;
    }
}
