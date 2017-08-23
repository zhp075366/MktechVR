package com.gotech.vrplayer.utils;

import java.text.DecimalFormat;

/**
 * Author: ZouHaiping on 2017/8/2
 * E-Mail: haiping.zou@gotechcn.cn
 * Desc:
 */
public class Constants {

    public static final int ONE_KB_SIZE = 1024;
    public static final int ONE_MB_SIZE = 1024 * 1024;
    public static final int ONE_GB_SIZE = 1024 * 1024 * 1024;

    public static final DecimalFormat ONE_DECIMAL_FORMAT = new DecimalFormat("0.0");
    public static final DecimalFormat TWO_DECIMAL_FORMAT = new DecimalFormat("0.00");

    // OkGo异常类
    public static final String BREAKPOINT_NOT_EXIST = "breakpoint file does not exist!";
    public static final String BREAKPOINT_EXPIRED = "breakpoint file has expired!";
    public static final String UNEXPECTED_END = "unexpected end of stream";

}
