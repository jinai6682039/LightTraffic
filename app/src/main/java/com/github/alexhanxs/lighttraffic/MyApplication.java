package com.github.alexhanxs.lighttraffic;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public class MyApplication extends Application {

    private static Context sApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        setsApplicationContext(this);
    }

    public static Context getsApplicationContext() {
        return sApplicationContext;
    }

    public static void setsApplicationContext(Context sApplicationContext) {
        MyApplication.sApplicationContext = sApplicationContext;
    }
}
