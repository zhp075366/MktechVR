package com.mktech.smarthome.model;

/**
 * Author: ZouHaiping on 2017/6/23
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public interface IVideoChannelModel {

    void loadMore(int page, String channelCode);

    void getFirstLoadData(String channelCode);

}
