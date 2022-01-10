package com.healthy.umfit;
import com.bugsnag.android.Bugsnag;

import android.app.Application;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        Bugsnag.start(this);
    }
}
