package com.gotech.vrplayer.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gotech.vrplayer.utils.MigrationHelper;

import org.greenrobot.greendao.database.Database;

public class AppMRHOpenHelper extends DaoMaster.OpenHelper {

    public AppMRHOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, UserBeanDao.class);
        //MigrationHelper.migrate(db, UserBeanDao.class, xxxxDao.class);
    }
}
