package com.flesh.washingtonhearld.app;

import com.flesh.washingtonhearld.app.Objects.User;

/**
 * Created by aaronfleshner on 5/16/14.
 */
public class WashingtonHearldSingleton {

    public static WashingtonHearldSingleton instance;
    public String accessToken = "";
    public User user = new User();

    public static void initInstance() {
        if (instance == null) {
            // Create the instance
            instance = new WashingtonHearldSingleton();
        }
    }

    public static WashingtonHearldSingleton getInstance() {
        // Return the instance
        return instance;
    }
}
