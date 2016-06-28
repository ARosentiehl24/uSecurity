package com.arrg.android.app.usecurity;

import android.os.Bundle;
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

    private ArrayList<Fragment> fragments;
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
        Util.setImmersiveMode(this);

        updateText(PIN, R.string.request_pin_message);
        updateText(PATTERN, R.string.request_pattern_message);
        updateText(FINGERPRINT, R.string.fingerprint_setting_message);

        fragments = new ArrayList<>();
        fragments.add(RequestPinFragment.newInstance());
        fragments.add(RequestPatternFragment.newInstance());

        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        if (fingerprintManagerCompat.isHardwareDetected()) {
            fragments.add(RequestFingerprintFragment.newInstance());
        }

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
                        btnAux.setText(R.string.enable_fingerprint_support);
                        tvRequestMessages.setText(fingerprintMessage);
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

                        updateText(R.string.request_pin_message);

                        ((RequestPinFragment) sectionsPagerAdapter.getItem(viewPager.getCurrentItem())).resetPinView();
                        break;
                    case PATTERN:
                        break;
                    case FINGERPRINT:
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
