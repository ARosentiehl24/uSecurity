package com.arrg.android.app.usecurity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.mukesh.permissions.AppPermissions;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

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

    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final int PIN = 0;
    public static final int PATTERN = 1;

    public static final int FINGERPRINT = 2;
    public static final int USAGE_STATS = 3;
    public static final int OVERLAY_PERMISSION = 4;
    public static final int WRITE_SETTINGS = 5;
    public static final int MEDIA_PERMISSION = 6;
    public static final int PHONE_PERMISSION = 7;
    public static final int WRITE_SETTINGS_PERMISSION_RC = 100;
    public static final int STORAGE_PERMISSION_RC = 101;
    public static final int OUTGOING_CALLS_PERMISSION_RC = 102;

    private AppPermissions appPermissions;
    private ArrayList<Boolean> list;
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

        appPermissions = new AppPermissions(this);
        fingerprintRecognition = PreferencesManager.getBoolean(getString(R.string.fingerprint_recognition_activated), false);
        list = new ArrayList<>();

        fragments = new ArrayList<>();
        fragments.add(RequestPinFragment.newInstance());
        fragments.add(RequestPatternFragment.newInstance());

        fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintManagerCompat.isHardwareDetected()) {
                fragments.add(RequestFingerprintFragment.newInstance());
                updateText(FINGERPRINT, R.string.fingerprint_setting_message);
            }
            fragments.add(RequestPermissionsFragment.newInstance(R.string.usage_stats_permission, R.drawable.ic_usage_stats, R.string.usage_stats_permission_description));
            fragments.add(RequestPermissionsFragment.newInstance(R.string.overlay_permission, R.drawable.ic_overlay_permission, R.string.overlay_permission_description));
            fragments.add(RequestPermissionsFragment.newInstance(R.string.write_settings_permission, R.drawable.ic_write_settings_permission, R.string.write_settings_permission_description));
            fragments.add(RequestPermissionsFragment.newInstance(R.string.media_permission, R.drawable.ic_media_permission, R.string.media_permission_description));
            fragments.add(RequestPermissionsFragment.newInstance(R.string.phone_permission, R.drawable.ic_phone_permission, R.string.phone_permission_description));
        } else {
            if (fingerprintManagerCompat.isHardwareDetected()) {
                fragments.add(RequestFingerprintFragment.newInstance());
                updateText(FINGERPRINT, R.string.fingerprint_setting_message);
            }
        }
        fragments.add(FinishFragment.newInstance());

        updateText(PATTERN, R.string.request_pattern_message);
        updateText(PIN, R.string.request_pin_message);

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

                btnAux.setVisibility(position != fragments.size() - 1 ? View.VISIBLE : View.INVISIBLE);
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
                        if (hasEnrolledFingerprints()) {
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
                    case WRITE_SETTINGS:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName())));
                            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        }
                        break;
                    case MEDIA_PERMISSION:
                        if (!appPermissions.hasPermission(STORAGE_PERMISSIONS)) {
                            appPermissions.requestPermission(STORAGE_PERMISSIONS, STORAGE_PERMISSION_RC);
                        } else {
                            Toast.makeText(this, R.string.all_permissions_granted, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PHONE_PERMISSION:
                        if (!appPermissions.hasPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                            appPermissions.requestPermission(Manifest.permission.PROCESS_OUTGOING_CALLS, OUTGOING_CALLS_PERMISSION_RC);
                        } else {
                            Toast.makeText(this, R.string.permission_already_granted, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;
            case R.id.btnNextDone:
                if (viewPager.getCurrentItem() != fragments.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    if (allSettingsAndPermissionsAreReady()) {
                        PreferencesManager.putBoolean(getString(R.string.settings_done), allSettingsAndPermissionsAreReady());

                        Navigator.with(this).build().goTo(ApplicationListActivity.class).animation().commit();
                        finish();
                    } else {
                        if (!pinWasConfigured()) {
                            viewPager.setCurrentItem(PIN);
                        } else if (!patternWasConfigured()) {
                            viewPager.setCurrentItem(PATTERN);
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!hasEnrolledFingerprints()) {
                                    viewPager.setCurrentItem(FINGERPRINT);
                                } else if (!usageStatsIsNotEmpty()) {
                                    viewPager.setCurrentItem(USAGE_STATS);
                                } else if (!overlayPermissionGranted()) {
                                    viewPager.setCurrentItem(OVERLAY_PERMISSION);
                                } else if (!writeSettingsPermissionGranted()) {
                                    viewPager.setCurrentItem(WRITE_SETTINGS);
                                } else if (!mediaPermissionGranted()) {
                                    viewPager.setCurrentItem(MEDIA_PERMISSION);
                                } else if (!phonePermissionGranted()) {
                                    viewPager.setCurrentItem(PHONE_PERMISSION);
                                }
                            } else {
                                if (!hasEnrolledFingerprints()) {
                                    viewPager.setCurrentItem(FINGERPRINT);
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_SETTINGS_PERMISSION_RC:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_PERMISSION_RC:
                List<Integer> permissionResults = new ArrayList<>();
                for (int grantResult : grantResults) {
                    permissionResults.add(grantResult);
                }
                if (permissionResults.contains(PackageManager.PERMISSION_DENIED)) {
                    Toast.makeText(this, R.string.all_permissions_not_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.all_permissions_granted, Toast.LENGTH_SHORT).show();
                }
                break;
            case OUTGOING_CALLS_PERMISSION_RC:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkForValues(){
        list.clear();

        list.add(pinWasConfigured());
        list.add(patternWasConfigured());
        list.add(hasEnrolledFingerprints());
        list.add(usageStatsIsNotEmpty());
        list.add(overlayPermissionGranted());
        list.add(writeSettingsPermissionGranted());
        list.add(mediaPermissionGranted());
        list.add(phonePermissionGranted());
    }

    public Boolean pinWasConfigured() {
        return userPin().length() != 0;
    }

    public String userPin() {
        return PreferencesManager.getString(getString(R.string.user_pin), "");
    }

    public Boolean patternWasConfigured() {
        return userPattern().length() != 0;
    }

    public String userPattern() {
        return PreferencesManager.getString(getString(R.string.user_pattern), "");
    }

    public Boolean hasEnrolledFingerprints() {
        return fingerprintManagerCompat.hasEnrolledFingerprints();
    }

    public Boolean usageStatsIsNotEmpty() {
        return !UsageStatsUtil.getUsageStatsList(this).isEmpty();
    }

    public Boolean overlayPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (Settings.canDrawOverlays(this));
        } else {
            return true;
        }
    }

    public Boolean writeSettingsPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(this);
        } else {
            return true;
        }
    }

    public Boolean mediaPermissionGranted() {
        return appPermissions.hasPermission(STORAGE_PERMISSIONS);
    }

    public Boolean phonePermissionGranted() {
        return appPermissions.hasPermission(Manifest.permission.PROCESS_OUTGOING_CALLS);
    }

    public Boolean allSettingsAndPermissionsAreReady() {
        checkForValues();

        return !list.contains(false);
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
