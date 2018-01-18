package com.mktech.smarthome.module.personal;

import android.content.Context;

import com.mktech.smarthome.utils.AppUtil;

/**
 * Author: ZouHaiping on 2017/6/19
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class PersonalPresenter {

    private Context mContext;
    private IPersonalView mView;

    public PersonalPresenter(Context context, IPersonalView view) {
        mContext = context;
        mView = view;
    }

    public String getVersionName() {
        return AppUtil.getVersionName(mContext);
    }
}
