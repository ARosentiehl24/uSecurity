package com.arrg.android.app.usecurity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.jaouan.revealator.Revealator;
import com.norbsoft.typefacehelper.ActionBarHelper;
import com.norbsoft.typefacehelper.TypefaceHelper;

import org.fingerlinks.mobile.android.navigator.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arrg.android.app.usecurity.USecurity.DURATIONS_OF_ANIMATIONS;

public class AppListActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.initialView)
    View initialView;
    @Bind(R.id.searchInput)
    AppCompatEditText searchInput;
    @Bind(R.id.revealView)
    LinearLayout revealView;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.appViewPager)
    ViewPager appViewPager;

    public static final int ALL_APPS_FRAGMENT = 0;
    public static final int APPS_LOCKED_FRAGMENT = 1;
    public static final int APPS_UNLOCKED_FRAGMENT = 2;

    private ArrayList<App> apps;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ButterKnife.bind(this);
        TypefaceHelper.typeface(this);

        setSupportActionBar(toolbar);

        ActionBarHelper.setTitle(getSupportActionBar(), TypefaceHelper.typeface(this, R.string.title_activity_app_list));

        apps = new ArrayList<>();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<App> filteredApps = new ArrayList<>();

                for (App app : apps) {
                    if (app.getAppName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredApps.add(app);
                    }
                }

                int position = appViewPager.getCurrentItem();

                AppListFragment appListFragment = (AppListFragment) sectionsPagerAdapter.getItem(position);
                appListFragment.setAdapter(position, filteredApps);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        new LoadApplications().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                Revealator.reveal(revealView)
                        .from(initialView)
                        .withRevealDuration(DURATIONS_OF_ANIMATIONS)
                        .withChildAnimationDuration(DURATIONS_OF_ANIMATIONS)
                        .withTranslateDuration(DURATIONS_OF_ANIMATIONS)
                        .withChildsAnimation()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                searchInput.requestFocus();
                                toggleKeyboard();
                            }
                        })
                        .start();
                break;
            case R.id.action_settings:
                Navigator.with(this).build().goTo(SettingsActivity.class).animation().commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnBack)
    public void onClick() {
        Revealator.unreveal(revealView)
                .withDuration(DURATIONS_OF_ANIMATIONS)
                .withEndAction(new TimerTask() {
                    @Override
                    public void run() {
                        searchInput.getText().clear();
                        toggleKeyboard();
                    }
                })
                .start();
    }

    public void toggleKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } else {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<App> apps;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<App> apps) {
            super(fm);
            this.apps = apps;
        }

        @Override
        public Fragment getItem(int position) {
            return AppListFragment.newInstance(position, apps);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case ALL_APPS_FRAGMENT:
                    return getString(R.string.apps);
                case APPS_LOCKED_FRAGMENT:
                    return getString(R.string.apps_locked);
                case APPS_UNLOCKED_FRAGMENT:
                    return getString(R.string.apps_unlocked);
            }
            return null;
        }
    }

    public ArrayList<App> getInstalledApplications(List<ApplicationInfo> applicationInfoList) {
        ArrayList<App> apps = new ArrayList<>();

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            try {
                if (getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                    App app = new App();
                    app.setAppIcon(applicationInfo.loadIcon(getPackageManager()));
                    app.setAppName(applicationInfo.loadLabel(getPackageManager()).toString());
                    app.setAppPackage(applicationInfo.packageName);
                    apps.add(app);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return apps;
    }

    public class LoadApplications extends AsyncTask<Void, Void, ArrayList<App>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<App> doInBackground(Void... voids) {
            ArrayList<App> apps = getInstalledApplications(getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA));

            Collections.sort(apps, new Comparator<App>() {
                @Override
                public int compare(App lhs, App rhs) {
                    return lhs.getAppName().compareTo(rhs.getAppName());
                }
            });

            return apps;
        }

        @Override
        protected void onPostExecute(ArrayList<App> apps) {
            super.onPostExecute(apps);

            AppListActivity.this.apps = apps;

            sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), apps);

            appViewPager.setAdapter(sectionsPagerAdapter);

            tabLayout.setupWithViewPager(appViewPager);
        }
    }
}
