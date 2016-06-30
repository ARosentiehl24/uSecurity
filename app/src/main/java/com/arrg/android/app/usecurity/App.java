package com.arrg.android.app.usecurity;

import android.graphics.drawable.Drawable;

import com.cardinalsolutions.sectioned_adapter.Categorizable;

import java.io.Serializable;

public class App implements Categorizable, Serializable {

    private Boolean isChecked = false;
    private Drawable appIcon;
    private String appName;
    private String appPackage;

    public App() {

    }

    public App(Drawable appIcon, String appName, String appPackage) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appPackage = appPackage;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    @Override
    public String getCategory() {
        return appName.substring(0, 1);
    }
}
