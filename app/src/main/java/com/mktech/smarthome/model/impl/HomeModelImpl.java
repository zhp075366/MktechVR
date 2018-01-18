package com.mktech.smarthome.model.impl;

import com.mktech.smarthome.R;
import com.mktech.smarthome.event.HomeMessageEvent;
import com.mktech.smarthome.model.IHomeModel;
import com.mktech.smarthome.model.bean.HomeAdBean;
import com.mktech.smarthome.model.bean.HomeCategoryBean;
import com.mktech.smarthome.model.bean.HomeMultipleItemBean;
import com.mktech.smarthome.model.bean.HomePictureBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZouHaiping on 2017/6/22
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class HomeModelImpl implements IHomeModel {

    // 主页Picture数据
    private List<HomePictureBean> getPictureData() {
        List<HomePictureBean> arr = new ArrayList<>();

        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "月の星く雪", "完结来补"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg", "影·蓝玉", "一看评论被***了一脸，伐开心。"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png", "i琳夏i", "(｀・ω・´)"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png", "Minerva。", "为啥下载不能了？π_π"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg", "", ""));

        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "如歌行极", "求生肉（/TДT)/"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg", "GERM", "第一次看 看弹幕那些说什么影帝模式啥的 感觉日了狗了 让我怎么往后看啊 艹"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png", "じ★ve↘魅惑", "开头吾王裙子被撩起来怎么回事！→_→"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png", "道尘一梦", "@伪 · 卫宫士郎"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png", "", ""));

        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "潘多哥斯拉", "朋友，听说过某R吗……..我呸，听说过虫群吗？(｀・ω・´)"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg", "一只红发的猫", "道理我都懂，我就问，几楼开车←_←"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png", "Mikuの草莓胖次", "扶..扶我起来,喝了最后这一瓶营养快线，让我撸死up"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png", "Absolute Field", "朋也，看过里番吗？"));
        arr.add(new HomePictureBean("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png", "", ""));

        return arr;
    }

    private List<HomeMultipleItemBean> getTestData() {
        int index = 0;
        List<HomeMultipleItemBean> resList = new ArrayList<>();
        String[] recommendTitle = new String[]{"每日精选", "热播榜首", "用户精选"};
        List<HomePictureBean> pictureList = getPictureData();

        for (int i = 0; i < recommendTitle.length; i++) {
            // 添加title
            HomeMultipleItemBean title = new HomeMultipleItemBean(HomeMultipleItemBean.TITLE, HomeMultipleItemBean.TITLE_SPAN_SIZE, recommendTitle[i]);
            resList.add(title);
            // 添加picture
            for (int j = 0; j < 4; j++) {
                HomeMultipleItemBean picture = new HomeMultipleItemBean(HomeMultipleItemBean.PICTURE, HomeMultipleItemBean.PICTURE_SPAN_SIZE, pictureList.get(index + j));
                resList.add(picture);
            }
            // 添加wide picture
            index = index + 4;
            HomeMultipleItemBean widePicture = new HomeMultipleItemBean(HomeMultipleItemBean.WIDE_PICTURE, HomeMultipleItemBean.WIDE_PICTURE_SPAN_SIZE, pictureList.get(index));
            resList.add(widePicture);
            index = index + 1;
        }

        return resList;

    }

    // 所有的推荐数据，包括recommendTitle，picture，wide picture
    @Override
    public void getFirstLoadData() {
        // 模拟后台获取数据任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<HomeMultipleItemBean> arrAll = getTestData();
                HomeMessageEvent<HomeMultipleItemBean> event = new HomeMessageEvent<>(HomeMessageEvent.EVENT_TYPE.TYPE_FIRST_LOAD_DATA);
                event.setData(arrAll);
                EventBus.getDefault().post(event);
            }
        }).start();
    }

    // 主页中间部分的分类
    @Override
    public List<HomeCategoryBean> getCategoryData() {
        List<HomeCategoryBean> categoryData = new ArrayList<>();
        String[] typeName = new String[]{"排行榜首", "创意广告", "经典动画", "优质短片", "极限运动"};
        int[] imageId = new int[]{R.mipmap.ic_home_category_one, R.mipmap.ic_home_category_two, R.mipmap.ic_home_category_three, R.mipmap.ic_home_category_four, R.mipmap.ic_home_category_five};

        for (int i = 0; i < typeName.length; i++) {
            HomeCategoryBean recommend = new HomeCategoryBean(typeName[i], imageId[i]);
            categoryData.add(recommend);
        }

        return categoryData;
    }

    // 主页ViewPager的数据
    @Override
    public List<HomeAdBean> getViewPagerData() {
        ArrayList<HomeAdBean> arr = new ArrayList<>();
        arr.add(new HomeAdBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "http://www" + ".bilibili.com/topic/v2/1004.html"));
        arr.add(new HomeAdBean("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg", "http://www" + ".bilibili.com/topic/1003.html"));
        arr.add(new HomeAdBean("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png", "http://yoo" + ".bilibili.com/html/activity/cq2015/index.html"));
        arr.add(new HomeAdBean("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png", "http://www" + ".bilibili.com/html/activity-acsociety.html"));
        return arr;
    }

    // 主页换一换的替换数据
    @Override
    public List<HomeMultipleItemBean> getReplaceRecommendData() {
        List<HomePictureBean> arr = new ArrayList<>();
        List<HomeMultipleItemBean> resList = new ArrayList<>();
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "Absolute Field", "朋也，看过里番吗？"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "Absolute Field", "朋也，看过里番吗？"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "Absolute Field", "朋也，看过里番吗？"));
        arr.add(new HomePictureBean("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "Absolute Field", "朋也，看过里番吗？"));

        for (int i = 0; i < 4; i++) {
            HomeMultipleItemBean picture = new HomeMultipleItemBean(HomeMultipleItemBean.PICTURE, HomeMultipleItemBean.PICTURE_SPAN_SIZE, arr.get(i));
            resList.add(picture);
        }

        return resList;
    }
}
