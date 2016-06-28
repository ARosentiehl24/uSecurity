package com.arrg.android.app.usecurity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.norbsoft.typefacehelper.TypefaceHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.fingerlinks.mobile.android.navigator.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IntroductionActivity extends AppCompatActivity {

    @Bind(R.id.tvAppName)
    AppCompatTextView tvAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
        TypefaceHelper.typeface(this);

        tvAppName.setText(String.format(getString(R.string.welcome_to_usecurity), getString(R.string.app_name)));

        boolean settingsDone = PreferencesManager.getBoolean(getString(R.string.settings_done), false);

        if (settingsDone) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Navigator.with(IntroductionActivity.this).build().goTo(ApplicationListActivity.class).animation().commit();
                    finish();
                }
            }, 250);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Navigator.with(IntroductionActivity.this).build().goTo(PresentationActivity.class).animation().commit();
                    finish();
                }
            }, 1250);
        }
    }
}
