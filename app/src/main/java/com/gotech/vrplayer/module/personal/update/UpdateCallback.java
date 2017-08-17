package com.gotech.vrplayer.module.personal.update;

public interface UpdateCallback {
    
    void onDownloadUpdateFail();

    void onDownloadUpdateStart();

    void onDownloadUpdateSuccess();

    void onDownloadUpdateProgress(int nProgress);

    void onCheckUpdateStart();

    void onCheckUpdateComplete(CheckUpdateThread.CheckUpdateMsg updateMsg);
}