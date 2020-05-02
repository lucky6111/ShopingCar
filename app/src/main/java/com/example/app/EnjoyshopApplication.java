package com.example.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.util.Log;

import com.example.app.bean.User;
import com.example.app.data.dao.DaoMaster;
import com.example.app.data.dao.DaoSession;
import com.example.app.service.LocationService;
import com.example.app.utils.UserLocalData;
import com.example.app.utils.Utils;
import com.mob.MobApplication;
import com.mob.MobSDK;

import java.util.HashMap;

public class EnjoyshopApplication extends MobApplication {
    //    mob信息    app key:201f8a7a91c30      App Secret:  c63ec5c1eeac1f873ec78c1365120431
    //百度地圖的 ak   zbqExff1uz8XyUVn5GbyylomCa0rOkmP

    public LocationService locationService;
    public Vibrator mVibrator;

    //整个app的上下文
    public static Context sContext;

    private static EnjoyshopApplication mInstance;

    public static EnjoyshopApplication getInstance() {
        return mInstance;
    }

    private User user;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // 通過代碼註冊你的AppKey和AppSecret
        MobSDK.init(this, "201f8a7a91c30", "c63ec5c1eeac1f873ec78c1365120431");

        sContext = getApplicationContext();
        initUser();
        Utils.init(this);

        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        setDatabase();

    }

    public void jumpToTargetActivity(Context context) {
        context.startActivity(intent);
        this.intent = null;
    }

    public static EnjoyshopApplication getApplication() {
        return mInstance;
    }

    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }

    public User getUser() {
        return user;
    }

    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    /**
     * 設置greenDao
     */
    private void setDatabase() {
        // 通過 DaoMaster 的內部類 DevOpenHelper，你可以得到一個便利的 SQLiteOpenHelper 對象。
        // 可能你已經註意到了，你並不需要去編寫「CREATE TABLE」這樣的 SQL 語句，因為 greenDAO 已經幫你做了。
        // 注意：默認的 DaoMaster.DevOpenHelper 會在數據庫升級時，刪除所有的表，意味著這將導致數據的丟失。
        // 所以，在正式的項目中，你還應該做一層封裝，來實現數據庫的安全升級。

        mHelper = new DaoMaster.DevOpenHelper(this, "shop-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：該數據庫連接屬於 DaoMaster，所以多個 Session 指的是相同的數據庫連接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public String getToken() {
        return UserLocalData.getToken(this);
    }

    private Intent intent;

    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }
}
