package com.arrg.android.app.usecurity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shawnlin.preferencesmanager.PreferencesManager;

import org.fingerlinks.mobile.android.navigator.Navigator;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        boolean settingsDone = PreferencesManager.getBoolean(getString(R.string.settings_done), false);

        if (settingsDone) {
            Navigator.with(this).build().goTo(ApplicationListActivity.class).animation().commit();
        } else {
            Navigator.with(this).build().goTo(PresentationActivity.class).animation().commit();
        }
    }
}
