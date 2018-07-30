package com.alice.core.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Admin on 2018/4/17.
 */

public class MarsApplication extends Application {
    private static MarsApplication sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        Fresco.initialize(this);
    }

    public static MarsApplication getInstance(){
       return sApplication;
    }
}
