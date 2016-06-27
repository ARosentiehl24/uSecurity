package com.arrg.android.app.usecurity;

import android.app.Application;
import android.content.Context;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;

public class USecurity extends Application {

    private ArrayList<TypefaceCollection> typefaceCollections;
    private PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();

        preferencesManager = new PreferencesManager(this);
        setPreferencesManager(getPackageName() + ".Settings");

        typefaceCollections = new ArrayList<>();
    }

    public void setPreferencesManager(String name) {
        preferencesManager.setName(name);
        preferencesManager.setMode(Context.MODE_PRIVATE);
        preferencesManager.init();
    }

    public TypefaceCollection getTypeface(int index) {
        return typefaceCollections.get(index);
    }
}
