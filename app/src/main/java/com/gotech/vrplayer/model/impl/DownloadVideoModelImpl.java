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

        //        DownloadVideoBean largeFile1 = new DownloadVideoBean();
        //        largeFile1.showName = "VMware-workstation";
        //        largeFile1.saveName = "VMware-workstation_save.exe";
        //        largeFile1.iconUrl = "";
        //        largeFile1.downUrl = "http://sw.bos.baidu.com/sw-search-sp/software/aff3469fe5f99/VMware-workstation-full-12.5.7.20721.exe";
        //        videos.add(largeFile1);
        //
        //        DownloadVideoBean largeFile2 = new DownloadVideoBean();
        //        largeFile2.showName = "android-studio-bundle";
        //        largeFile2.saveName = "android-studio-bundle_save.exe";
        //        largeFile2.iconUrl = "";
        //        largeFile2.downUrl = "http://sw.bos.baidu.com/sw-search-sp/software/5f5ecfa13d98c/androidstudio_2.3.0.0.exe";
        //        videos.add(largeFile2);
        //
        //        DownloadVideoBean apk1 = new DownloadVideoBean();
        //        apk1.showName = "爱奇艺";
        //        apk1.saveName = "com.qiyi.video_save.apk";
        //        apk1.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";
        //        apk1.downUrl = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";
        //        videos.add(apk1);
        //
        //        DownloadVideoBean apk2 = new DownloadVideoBean();
        //        apk2.showName = "微信";
        //        apk2.saveName = "com.tencent.mm_save.apk";
        //        apk2.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/00814b5dad9b54cc804466369c8cb18f23e23823f";
        //        apk2.downUrl = "http://116.117.158.129/f2.market.xiaomi.com/download/AppStore/04275951df2d94fee0a8210a3b51ae624cc34483a/com.tencent.mm.apk";
        //        videos.add(apk2);
        //
        //        DownloadVideoBean apk3 = new DownloadVideoBean();
        //        apk3.showName = "新浪微博";
        //        apk3.saveName = "com.sina.weibo_save.apk";
        //        apk3.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/01db44d7f809430661da4fff4d42e703007430f38";
        //        apk3.downUrl = "http://60.28.125.129/f1.market.xiaomi.com/download/AppStore/0ff41344f280f40c83a1bbf7f14279fb6542ebd2a/com.sina.weibo.apk";
        //        videos.add(apk3);
        //
        //        DownloadVideoBean apk4 = new DownloadVideoBean();
        //        apk4.showName = "QQ";
        //        apk4.saveName = "com.tencent.mobileqq_save.apk";
        //        apk4.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/072725ca573700292b92e636ec126f51ba4429a50";
        //        apk4.downUrl = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0ff0604fd770f481927d1edfad35675a3568ba656/com.tencent.mobileqq.apk";
        //        videos.add(apk4);
        //
        //        DownloadVideoBean apk5 = new DownloadVideoBean();
        //        apk5.showName = "陌陌";
        //        apk5.saveName = "com.immomo.momo_save.apk";
        //        apk5.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/06006948e655c4dd11862d060bd055b4fd2b5c41b";
        //        apk5.downUrl = "http://121.18.239.1/f4.market.xiaomi.com/download/AppStore/096f34dec955dbde0597f4e701d1406000d432064/com.immomo.momo.apk";
        //        videos.add(apk5);
        //
        //        DownloadVideoBean apk6 = new DownloadVideoBean();
        //        apk6.showName = "手机淘宝";
        //        apk6.saveName = "com.taobao.taobao_save.apk";
        //        apk6.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/017a859792d09d7394108e0a618411675ec43f220";
        //        apk6.downUrl = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0afc00452eb1a4dc42b20c9351eacacab4692a953/com.taobao.taobao.apk";
        //        videos.add(apk6);
        //
        //        DownloadVideoBean apk7 = new DownloadVideoBean();
        //        apk7.showName = "酷狗音乐";
        //        apk7.saveName = "com.kugou.android_save.apk";
        //        apk7.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0f2f050e21e42f75c7ecca55d01ac4e5e4e40ca8d";
        //        apk7.downUrl = "http://121.18.239.1/f5.market.xiaomi.com/download/AppStore/053ed49c1545c6eec3e3e23b31568c731f940934f/com.kugou.android.apk";
        //        videos.add(apk7);
        //
        //        DownloadVideoBean apk8 = new DownloadVideoBean();
        //        apk8.showName = "网易云音乐";
        //        apk8.saveName = "com.netease.cloudmusic_save.apk";
        //        apk8.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/02374548ac39f3b7cdbf5bea4b0535b5d1f432f23";
        //        apk8.downUrl = "http://121.18.239.1/f4.market.xiaomi.com/download/AppStore/0f458c5661acb492e30b808a2e3e4c8672e6b55e2/com.netease.cloudmusic.apk";
        //        videos.add(apk8);
        //
        //        DownloadVideoBean apk9 = new DownloadVideoBean();
        //        apk9.showName = "ofo共享单车";
        //        apk9.saveName = "so.ofo.labofo_save.apk";
        //        apk9.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0fe1a5c6092f3d9fa5c4c1e3158e6ff33f6418152";
        //        apk9.downUrl = "http://60.28.125.1/f4.market.mi-img.com/download/AppStore/06954949fcd48414c16f726620cf2d52200550f56/so.ofo.labofo.apk";
        //        videos.add(apk9);
        //
        //        DownloadVideoBean apk10 = new DownloadVideoBean();
        //        apk10.showName = "摩拜单车";
        //        apk10.saveName = "com.mobike.mobikeapp_save.apk";
        //        apk10.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0863a058a811148a5174d9784b7be2f1114191f83";
        //        apk10.downUrl = "http://60.28.125.1/f4.market.xiaomi.com/download/AppStore/00cdeb4865c5a4a7d350fe30b9f812908a569cc8a/com.mobike.mobikeapp.apk";
        //        videos.add(apk10);
        //
        //        DownloadVideoBean apk11 = new DownloadVideoBean();
        //        apk11.showName = "贪吃蛇大作战";
        //        apk11.saveName = "com.wepie.snake.new.mi_save.apk";
        //        apk11.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/09f7f5756d9d63bb149b7149b8bdde0769941f09b";
        //        apk11.downUrl = "http://60.22.46.1/f3.market.xiaomi.com/download/AppStore/0b02f24ffa8334bd21b16bd70ecacdb42374eb9cb/com.wepie.snake.new.mi.apk";
        //        videos.add(apk11);
        //
        //        DownloadVideoBean apk12 = new DownloadVideoBean();
        //        apk12.showName = "蘑菇街";
        //        apk12.saveName = "com.mogujie_save.apk";
        //        apk12.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0ab53044735e842c421a57954d86a77aea30cc1da";
        //        apk12.downUrl = "http://121.29.10.1/f5.market.xiaomi.com/download/AppStore/07a6ee4955e364c3f013b14055c37b8e4f6668161/com.mogujie.apk";
        //        videos.add(apk12);
        //
        DownloadVideoBean apk13 = new DownloadVideoBean();
        apk13.showName = "聚美优品";
        apk13.saveName = "com.jm.android.jumei_save.apk";
        apk13.iconUrl = "";
        apk13.downUrl = "http://gdown.baidu.com/data/wisegame/5998b64e9e606ccb/jumeiyoupin_4652.apk";
        apk13.totalSize = 50209651;
        videos.add(apk13);

        DownloadVideoBean apk14 = new DownloadVideoBean();
        apk14.showName = "酷狗音乐";
        apk14.saveName = "kugouyinle_save.apk";
        apk14.iconUrl = "";
        apk14.downUrl = "http://gdown.baidu.com/data/wisegame/ca9305e85a08b26d/kugouyinle_8851.apk";
        apk14.totalSize = 45741101;
        videos.add(apk14);

        DownloadVideoBean apk15 = new DownloadVideoBean();
        apk15.showName = "书旗小说";
        apk15.saveName = "com.shuqi.controller_save.apk";
        apk15.iconUrl = "";
        apk15.downUrl = "http://gdown.baidu.com/data/wisegame/7bea1eb26b14f294/shuqixiaoshuo_138.apk";
        apk15.totalSize = 28495747;
        videos.add(apk15);

        DownloadVideoBean apk16 = new DownloadVideoBean();
        apk16.showName = "有道云笔记";
        apk16.saveName = "youdaoyunbiji_save.apk";
        apk16.iconUrl = "";
        apk16.downUrl = "http://gdown.baidu.com/data/wisegame/bba90a8f9b03d5bd/youdaoyunbiji_77.apk";
        apk16.totalSize = 57624036;
        videos.add(apk16);

        return videos;
    }
}
