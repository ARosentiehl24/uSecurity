package com.arrg.android.app.usecurity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class IntroductionActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }

            @Override
            public boolean canGoBackward(int position) {
                return true;
            }
        });

        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {

            }
        });

        addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
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

        addSlide(new FragmentSlide.Builder().background(R.color.colorPrimary).backgroundDark(R.color.colorPrimary).fragment(RequestPinFragment.newInstance()).build());
    }
}
