package com.ericwyn.quickopen;

import android.graphics.drawable.Drawable;

public class MyAppInfo {
    private Drawable image;
    private String appName;
    private String packageName;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
