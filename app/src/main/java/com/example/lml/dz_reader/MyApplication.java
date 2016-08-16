package com.example.lml.dz_reader;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by lml on 2016/8/16.
 */
public class MyApplication extends Application {

    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
    }
}
