package com.arrg.android.app.usecurity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.badoualy.stepperindicator.StepperIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PresentationActivity extends AppCompatActivity {

    @Bind(R.id.stepperIndicator)
    StepperIndicator stepperIndicator;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        ButterKnife.bind(this);
        com.norbsoft.typefacehelper.TypefaceHelper.typeface(this);

        fragments = new ArrayList<>();
        fragments.add(WelcomeFragment.newInstance());
        fragments.add(RequestPinFragment.newInstance());
        fragments.add(RequestPatternFragment.newInstance());

        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        if (fingerprintManagerCompat.isHardwareDetected()) {
            fragments.add(RequestFingerprintFragment.newInstance());
        }

        fragments.add(FinishFragment.newInstance());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        stepperIndicator.setViewPager(viewPager, fragments.size() - 1);
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
