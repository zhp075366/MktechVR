package com.gotech.vrplayer;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.gotech.vrplayer.greendao.AppMRHOpenHelper;
import com.gotech.vrplayer.greendao.DaoMaster;
import com.gotech.vrplayer.greendao.DaoSession;
import com.gotech.vrplayer.utils.MigrationHelper;
import com.lzy.okgo.OkGo;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;

/**
 * 作者：Zou Haiping on 2016/10/9 10:50
 * 邮箱：zhp075366@163.com
 * 公司：MKTech
 */
public class VRApplication extends Application {

    private DaoSession mDaoSession;
    private static VRApplication mApp;
    private static final String GLOBAL_TAG = "iot";

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        initDataBase();
        initLeakCanary();
        KLog.init(true, GLOBAL_TAG);
        OkGo.getInstance().init(this);
    }

    public static VRApplication getApplication() {
        return mApp;
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 设置greenDAO
     * 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象
     * 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了
     * 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失
     * 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级，即下面的 RxMRHOpenHelper 类
     * <p>
     * DevOpenHelper：创建SQLite数据库的SQLiteOpenHelper的具体实现
     * DaoMaster：GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
     * DaoSession：管理所有的Dao对象，Dao对象中有增删改查等API
     */
    private void initDataBase() {
        MigrationHelper.DEBUG = true;
        //创建数据库 test.db
        AppMRHOpenHelper helper = new AppMRHOpenHelper(this, "test.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        //比如获取UserDao对象
        //mUserDao = VRApplication.getInstances().getDaoSession().getUserDao();
        return mDaoSession;
    }
}
