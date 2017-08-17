package com.gotech.vrplayer.module.personal.update;

public class MessageID {

    private static final int MESSAGE_ID_START = 0x00000000;
    public static final int AUTO_UPDATE_SERVICE_CONNECTED = MESSAGE_ID_START + 1;
    public static final int AUTO_UPDATE_CHECKING_COMPLETE = AUTO_UPDATE_SERVICE_CONNECTED + 1;
    public static final int AUTO_UPDATE_SERVICE_DISCONNECTED = AUTO_UPDATE_CHECKING_COMPLETE + 1;
    public static final int AUTO_UPDATE_DOWNLOADING_COMPLETE = AUTO_UPDATE_SERVICE_DISCONNECTED + 1;

}
