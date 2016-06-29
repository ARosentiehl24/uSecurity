package com.arrg.android.app.usecurity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jaouan.revealator.Revealator;
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
import static com.arrg.android.app.usecurity.USecurity.LOCKED_APPS_PREFERENCES;
import static com.arrg.android.app.usecurity.USecurity.PACKAGES_APPS_PREFERENCES;

public class ApplicationListActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.initialView)
    View initialView;
    @Bind(R.id.searchInput)
    AppCompatEditText searchInput;
    @Bind(R.id.revealView)
    LinearLayout revealView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.sideBar)
    SideBar sideBar;

    private ArrayList<App> apps;
    private Integer index;
    private SharedPreferences lockedAppsPreferences;
    private SharedPreferences packagesAppsPreferences;
    private SharedPreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        ButterKnife.bind(this);
        TypefaceHelper.typeface(this);

        setSupportActionBar(toolbar);

        apps = new ArrayList<>();
        preferencesUtil = new SharedPreferencesUtil(this);
        lockedAppsPreferences = getSharedPreferences(LOCKED_APPS_PREFERENCES, Context.MODE_PRIVATE);
        packagesAppsPreferences = getSharedPreferences(PACKAGES_APPS_PREFERENCES, Context.MODE_PRIVATE);

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

                AppAdapter appAdapter = new AppAdapter(ApplicationListActivity.this, filteredApps, lockedAppsPreferences, packagesAppsPreferences, preferencesUtil);
                recyclerView.setAdapter(appAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                index = 0;

                for (App app : apps){
                    if (app.getAppName().substring(0, 1).toUpperCase().equals(s.toUpperCase())) {
                        break;
                    }
                    index++;
                }

                recyclerView.scrollToPosition(index);

                Log.e("Scroll", "Child: " + recyclerView.getChildCount());
            }
        });

        new LoadApplications().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_application_list, menu);
        return super.onCreateOptionsMenu(menu);
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
                        AppAdapter appAdapter = new AppAdapter(ApplicationListActivity.this, apps, lockedAppsPreferences, packagesAppsPreferences, preferencesUtil);
                        recyclerView.setAdapter(appAdapter);
                    }
                })
                .start();
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

            ApplicationListActivity.this.apps = apps;

            AppAdapter appAdapter = new AppAdapter(ApplicationListActivity.this, apps, lockedAppsPreferences, packagesAppsPreferences, preferencesUtil);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
            recyclerView.setAdapter(appAdapter);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }
}
