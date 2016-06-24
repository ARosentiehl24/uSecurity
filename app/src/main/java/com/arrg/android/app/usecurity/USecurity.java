package com.arrg.android.app.usecurity;

import android.app.Application;

import com.norbsoft.typefacehelper.TypefaceCollection;

import java.util.ArrayList;

public class USecurity extends Application {

    private ArrayList<TypefaceCollection> typefaceCollections;

    @Override
    public void onCreate() {
        super.onCreate();
        typefaceCollections = new ArrayList<>();
    }

    public TypefaceCollection getTypeface(int index) {
        return typefaceCollections.get(index);
    }
}
