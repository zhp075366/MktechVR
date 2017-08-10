package com.gotech.vrplayer.model.impl;

import com.gotech.vrplayer.model.bean.HomePictureBean;
import com.gotech.vrplayer.event.LoadingDataEvent;
import com.gotech.vrplayer.model.IVideoChannelModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/23
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class VideoChannelModelImpl implements IVideoChannelModel {

    @Override
    public void loadMore(final int page, final String channelCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                List<HomePictureBean> data = getData(page);
                LoadingDataEvent.EVENT_TYPE type = LoadingDataEvent.EVENT_TYPE.TYPE_LOAD_MORE_DATA;
                LoadingDataEvent event = new LoadingDataEvent(type);
                event.setEventCode(channelCode);
                event.setData(data);
                EventBus.getDefault().post(event);
            }
        }).start();
    }

    @Override
    public void getFirstLoadData(final String channelCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                List<HomePictureBean> data = getData(0);
                LoadingDataEvent.EVENT_TYPE type = LoadingDataEvent.EVENT_TYPE.TYPE_FIRST_LOAD_DATA;
                LoadingDataEvent event = new LoadingDataEvent(type);
                event.setEventCode(channelCode);
                event.setData(data);
                EventBus.getDefault().post(event);
            }
        }).start();
    }

    // 视频页，视频详情页，EasyRecyclerView预加载数据
    private List<HomePictureBean> getData(int page) {
        List<HomePictureBean> arr = new ArrayList<>();
        if(page >= 5) {
            return null;
        }
        arr.add(new HomePictureBean("http://i2.hdslb.com/52_52/user/61175/6117592/myface.jpg", "月の星く雪" + "————————第" + page + "页", "完结来补"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/52_52/user/6738/673856/myface.jpg", "影·蓝玉", "一看评论被***了一脸，伐开心。"));
        arr.add(new HomePictureBean("http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "i琳夏i", "(｀・ω・´)"));
        arr.add(new HomePictureBean("http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "Minerva。", "为啥下载不能了？π_π"));
        arr.add(new HomePictureBean("http://i0.hdslb.com/52_52/account/face/4613528/303f4f5a/myface.png", "如歌行极", "求生肉（/TДT)/"));
        arr.add(new HomePictureBean("http://i0.hdslb.com/52_52/account/face/611203/76c02248/myface.png", "GERM", "第一次看 看弹幕那些说什么影帝模式啥的 感觉日了狗了 让我怎么往后看啊 艹"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/52_52/user/46230/4623018/myface.jpg", "じ★ve↘魅惑", "开头吾王裙子被撩起来怎么回事！→_→"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/52_52/user/66723/6672394/myface.jpg", "道尘一梦", "@伪 · 卫宫士郎"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/user/3039/303946/myface.jpg", "潘多哥斯拉", "朋友，听说过某R吗……..我呸，听说过虫群吗？(｀・ω・´)"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/account/face/9034989/aabbc52a/myface.png", "一只红发的猫", "道理我都懂，我就问，几楼开车←_←"));
        arr.add(new HomePictureBean("http://i0.hdslb.com/account/face/1557783/8733bd7b/myface.png", "Mikuの草莓胖次", "扶..扶我起来,喝了最后这一瓶营养快线，让我撸死up"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/user/3716/371679/myface.jpg", "Absolute Field", "朋也，看过里番吗？"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/52_52/user/6738/673856/myface.jpg", "影·蓝玉", "一看评论被***了一脸，伐开心。"));
        arr.add(new HomePictureBean("http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "i琳夏i", "(｀・ω・´)"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/52_52/user/61175/6117592/myface.jpg", "月の星く雪", "完结来补"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/52_52/user/46230/4623018/myface.jpg", "じ★ve↘魅惑", "开头吾王裙子被撩起来怎么回事！→_→"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/52_52/user/66723/6672394/myface.jpg", "道尘一梦", "@伪 · 卫宫士郎"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/user/3039/303946/myface.jpg", "潘多哥斯拉", "朋友，听说过某R吗……..我呸，听说过虫群吗？(｀・ω・´)"));
        return arr;
    }
}
