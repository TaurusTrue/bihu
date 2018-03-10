package com.example.taurus.bihu.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Taurus on 2018/2/20.
 * 用于全局获取context
 */

public class MyApplication extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
