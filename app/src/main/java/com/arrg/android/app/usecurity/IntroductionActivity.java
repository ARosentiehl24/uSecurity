package com.arrg.android.app.usecurity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.afollestad.assent.Assent;
import com.github.paolorotolo.appintro.AppIntro;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.fingerlinks.mobile.android.navigator.Navigator;

public class IntroductionActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assent.setActivity(this, this);

        boolean settingsDone = PreferencesManager.getBoolean(getString(R.string.settings_done), false);

        if (settingsDone) {
            Navigator.with(this).build().goTo(ApplicationListActivity.class).commit();
        } else {
            addSlide(RequestPinFragment.newInstance());
            addSlide(RequestPatternFragment.newInstance());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);

                if (fingerprintManagerCompat.isHardwareDetected()) {
                    addSlide(RequestFingerprintFragment.newInstance());
                }
            }

            setGoBackLock(true);
            showSkipButton(false);
            setSwipeLock(true);

            setBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            setSeparatorColor(ContextCompat.getColor(this, R.color.colorPrimary));

            getPager().setBackgroundResource(R.color.colorPrimary);

            setVibrate(true);
            setVibrateIntensity(10);
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        /*Navigator.with(this).build().goTo(ApplicationListActivity.class).animation().commit();
        finish();*/
        getPager().setCurrentItem(0);
    }
}
