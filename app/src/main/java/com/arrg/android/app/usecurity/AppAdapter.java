package com.arrg.android.app.usecurity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<App> apps;
    private SharedPreferences lockedAppsPreferences;
    private SharedPreferences packagesAppsPreferences;
    private SharedPreferencesUtil preferencesUtil;

    public AppAdapter(Activity context, ArrayList<App> apps, SharedPreferences lockedAppsPreferences, SharedPreferences packagesAppsPreferences, SharedPreferencesUtil preferencesUtil) {
        this.context = context;
        this.apps = apps;
        this.lockedAppsPreferences = lockedAppsPreferences;
        this.packagesAppsPreferences = packagesAppsPreferences;
        this.preferencesUtil = preferencesUtil;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View applications = inflater.inflate(R.layout.app_item, parent, false);

        return new ViewHolder(applications, context, apps, preferencesUtil, lockedAppsPreferences);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public void addApp(App app, int position) {
        apps.add(app);
        notifyItemInserted(position);
    }

    public void removeApp(int position) {
        apps.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ViewHolder(View itemView, Activity context, ArrayList<App> apps, SharedPreferencesUtil preferencesUtil, SharedPreferences lockedAppsPreferences) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
