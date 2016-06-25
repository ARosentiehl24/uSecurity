package com.arrg.android.app.usecurity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.afollestad.assent.Assent;
import com.github.paolorotolo.appintro.AppIntro;

public class IntroductionActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assent.setActivity(this, this);

        addSlide(RequestPinFragment.newInstance());
        addSlide(RequestPatternFragment.newInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            askForPermissions(new String[]{Manifest.permission.USE_FINGERPRINT}, 3);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            if (fingerprintManager.isHardwareDetected()) {
                addSlide(RequestFingerprintFragment.newInstance());
            }
        }

        setBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setSeparatorColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        getPager().setBackgroundResource(R.color.colorPrimary);

        setVibrate(true);
        setVibrateIntensity(10);
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
    }
}
