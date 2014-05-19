package com.flesh.washingtonhearld.app;

import android.app.Application;

/**
 * Created by aaronfleshner on 5/16/14.
 */
public class WashingtonHearldApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    protected void initSingletons() {
        // Initialize the instance of MySingleton
        WashingtonHearldSingleton.initInstance();
    }

}
