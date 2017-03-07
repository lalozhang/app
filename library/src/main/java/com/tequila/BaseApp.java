package com.tequila;

import android.app.Application;
import android.content.Context;

import com.tequila.cache.disk.ResponseDiskCache;

/**
 * Created by admin on 2017/3/7.
 */

public class BaseApp extends Application{

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        ResponseDiskCache.initialize(this);
    }

    public static Context getContext(){
        return appContext;
    }

}
