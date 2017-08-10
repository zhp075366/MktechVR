package com.gotech.vrplayer.model;

import com.gotech.vrplayer.model.bean.UserBean;

import java.util.List;

/**
 * 作者：Created by ZouHaiping on 2017/5/26
 * 邮箱：haiping.zou@gotechcn.cn
 * 公司：MKTech
 */
public interface IUserModel {

    void addUser(String name);

    void deleteUser(String name);

    void updateUser(String updateId, String updatedName);

    List<UserBean> loadAllUser();

    void deleteAllUser();
}
