package com.arrg.android.app.usecurity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        App app = apps.get(position);

        holder.appIcon.setImageDrawable(app.getAppIcon());
        holder.appName.setText(app.getAppName());
        holder.circleCheckBox.setEnabled(preferencesUtil.getBoolean(lockedAppsPreferences, app.getAppPackage(), false));
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
        @Bind(R.id.appIcon)
        AppCompatImageView appIcon;
        @Bind(R.id.appName)
        AppCompatTextView appName;
        @Bind(R.id.circleCheckBox)
        CircleCheckBox circleCheckBox;

        public ViewHolder(View itemView, Activity context, ArrayList<App> apps, SharedPreferencesUtil preferencesUtil, SharedPreferences lockedAppsPreferences) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            App app = apps.get(getLayoutPosition());
            app.setChecked(!app.getChecked());

            circleCheckBox.setChecked(app.getChecked());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
