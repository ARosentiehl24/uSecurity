package com.arrg.android.app.usecurity;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;

public class USecurity extends Application {

    public static Integer DURATIONS_OF_ANIMATIONS = 250;

    public static String PACKAGE_NAME;
    public static String LOCKED_APPS_PREFERENCES;
    public static String PACKAGES_APPS_PREFERENCES;
    public static String SETTINGS_PREFERENCES;

    private ArrayList<TypefaceCollection> typefaceCollections;
    private PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();

        PACKAGE_NAME = getPackageName().toUpperCase();

        LOCKED_APPS_PREFERENCES = PACKAGE_NAME + ".LOCKED_APPS";
        PACKAGES_APPS_PREFERENCES = PACKAGE_NAME + ".PACKAGES_APPS";
        SETTINGS_PREFERENCES = PACKAGE_NAME + ".SETTINGS";

        preferencesManager = new PreferencesManager(this);
        setPreferencesManager(SETTINGS_PREFERENCES);

        typefaceCollections = new ArrayList<>();
        typefaceCollections.add(new TypefaceCollection.Builder().set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/Raleway.ttf")).create());

        int fontPosition = PreferencesManager.getInt(getString(R.string.font_position), 0);

        initTypeFace(getTypeface(fontPosition));
    }

    public void setPreferencesManager(String name) {
        preferencesManager.setName(name);
        preferencesManager.setMode(Context.MODE_PRIVATE);
        preferencesManager.init();
    }

    public TypefaceCollection getTypeface(int index) {
        return typefaceCollections.get(index);
    }

    public void initTypeFace(TypefaceCollection typefaceCollection) {
        TypefaceHelper.init(typefaceCollection);
    }
}
