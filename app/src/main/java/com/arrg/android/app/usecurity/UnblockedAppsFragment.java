package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnblockedAppsFragment extends Fragment {


    public UnblockedAppsFragment() {
        // Required empty public constructor
    }

    public static UnblockedAppsFragment newInstance(int position, ArrayList<App> apps) {
        return new UnblockedAppsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unblocked_apps, container, false);
    }
}
