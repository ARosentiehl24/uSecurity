package com.arrg.android.app.usecurity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PresentationActivity extends AppCompatActivity {


    @Bind(R.id.stepperIndicator)
    StepperIndicator stepperIndicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.tvRequestMessages)
    AppCompatTextView tvRequestMessages;
    @Bind(R.id.btnPrevious)
    AppCompatButton btnPrevious;
    @Bind(R.id.btnNextDone)
    AppCompatButton btnNextDone;
    @Bind(R.id.btnAux)
    AppCompatButton btnAux;
    @Bind(R.id.main_content)
    LinearLayout mainContent;

    public static final int PIN = 0;
    public static final int PATTERN = 1;
    public static final int FINGERPRINT = 2;
    public static final int USAGE_STATS = 3;
    public static final int OVERLAY_PERMISSION = 4;

    private ArrayList<Fragment> fragments;
    private Boolean fingerprintRecognition;
    private FingerprintManagerCompat fingerprintManagerCompat;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private String pinMessage;
    private String patternMessage;
    private String fingerprintMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        ButterKnife.bind(this);
        TypefaceHelper.typeface(this);

        fingerprintRecognition = PreferencesManager.getBoolean(getString(R.string.fingerprint_recognition_activated), false);

        updateText(FINGERPRINT, R.string.fingerprint_setting_message);
        updateText(PATTERN, R.string.request_pattern_message);
        updateText(PIN, R.string.request_pin_message);

        fragments = new ArrayList<>();
        fragments.add(RequestPinFragment.newInstance());
        fragments.add(RequestPatternFragment.newInstance());

        fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        if (fingerprintManagerCompat.isHardwareDetected()) {
            fragments.add(RequestFingerprintFragment.newInstance());
        }

        fragments.add(RequestPermissionsFragment.newInstance(R.string.usage_stats_permission, R.drawable.ic_usage_stats, R.string.usage_stats_permission_description));
        fragments.add(RequestPermissionsFragment.newInstance(R.string.usage_stats_permission, R.drawable.ic_usage_stats, R.string.usage_stats_permission_description));
        fragments.add(FinishFragment.newInstance());

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case PIN:
                        btnAux.setText(R.string.reset_pin);
                        tvRequestMessages.setText(pinMessage);
                        break;
                    case PATTERN:
                        btnAux.setText(R.string.reset_pattern);
                        tvRequestMessages.setText(patternMessage);
                        break;
                    case FINGERPRINT:
                        btnAux.setText(fingerprintRecognition ? R.string.disable_fingerprint_support : R.string.enable_fingerprint_support);
                        tvRequestMessages.setText(fingerprintMessage);
                        break;
                    default:
                        btnAux.setText(R.string.grant_permission);
                        tvRequestMessages.setText(R.string.permissions_request_message);
                        break;
                }

                btnPrevious.setVisibility(position >= 1 ? View.VISIBLE : View.INVISIBLE);
                btnNextDone.setText(position != fragments.size() - 1 ? R.string.next : R.string.done);
                btnNextDone.setCompoundDrawablesWithIntrinsicBounds(null, null, position != fragments.size() - 1 ? ContextCompat.getDrawable(PresentationActivity.this, R.drawable.ic_chevron_right_midnight_blue_24dp) : null, null);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        stepperIndicator.setViewPager(viewPager, fragments.size() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setImmersiveMode(this);
    }

    @OnClick({R.id.btnPrevious, R.id.btnAux, R.id.btnNextDone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrevious:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                break;
            case R.id.btnAux:
                switch (viewPager.getCurrentItem()) {
                    case PIN:
                        PreferencesManager.putString(getString(R.string.user_pin), "");

                        updateText(PIN, R.string.request_pin_message);

                        ((RequestPinFragment) sectionsPagerAdapter.getItem(viewPager.getCurrentItem())).resetPinView();
                        break;
                    case PATTERN:
                        PreferencesManager.putString(getString(R.string.user_pattern), "");

                        updateText(PATTERN, R.string.request_pattern_message);

                        ((RequestPatternFragment) sectionsPagerAdapter.getItem(viewPager.getCurrentItem())).resetPattern();
                        break;
                    case FINGERPRINT:
                        if (fingerprintManagerCompat.hasEnrolledFingerprints()) {
                            if (fingerprintRecognition) {
                                Toast.makeText(this, R.string.fingerprint_disable_message, Toast.LENGTH_SHORT).show();

                                PreferencesManager.putBoolean(getString(R.string.fingerprint_recognition_activated), false);

                                btnAux.setText(R.string.enable_fingerprint_support);
                            } else {
                                Toast.makeText(this, R.string.fingerprint_enable_message, Toast.LENGTH_SHORT).show();

                                PreferencesManager.putBoolean(getString(R.string.fingerprint_recognition_activated), true);

                                btnAux.setText(R.string.disable_fingerprint_support);
                            }

                            fingerprintRecognition = PreferencesManager.getBoolean(getString(R.string.fingerprint_recognition_activated), false);
                        } else {
                            Toast.makeText(this, R.string.add_fingerprint_message, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                            startActivity(intent);
                        }
                        break;
                    case USAGE_STATS:
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        break;
                    case OVERLAY_PERMISSION:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
                            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        }
                        break;
                }
                break;
            case R.id.btnNextDone:
                if (viewPager.getCurrentItem() != fragments.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {

                }
                break;
        }
    }

    public void updateText(int position, int text) {
        switch (position) {
            case PIN:
                pinMessage = getString(text);
                break;
            case PATTERN:
                patternMessage = getString(text);
                break;
            case FINGERPRINT:
                fingerprintMessage = getString(text);
                break;
        }

        updateText(text);
    }

    public void updateText(int text) {
        tvRequestMessages.setText(text);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTag();
        }
    }
}
