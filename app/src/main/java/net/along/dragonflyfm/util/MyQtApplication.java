package net.along.dragonflyfm.util;

import android.app.Application;

import fm.qingting.qtsdk.QTSDK;

/**
 * 蜻蜓FM : Key 获取直播源最根本的方式
 * @author 每天都有最爱的傻子陪着
 */

public class MyQtApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        QTSDK.setHost("https://open.staging.qingting.fm");
        QTSDK.init(getApplicationContext(), "MmYxYThlY2EtYWMxMi0xMWU4LTkyM2YtMDAxNjNlMDAyMGFk"
        );
        QTSDK.setAuthRedirectUrl("http://qttest.qingting.fm");
    }
}
