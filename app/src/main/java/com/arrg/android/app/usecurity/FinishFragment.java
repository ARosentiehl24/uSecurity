package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinishFragment extends Fragment {


    @Bind(R.id.imageView)
    AppCompatImageView imageView;
    @Bind(R.id.description)
    AppCompatTextView description;

    private PresentationActivity presentationActivity;

    public FinishFragment() {
    }

    public static FinishFragment newInstance() {
        return new FinishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presentationActivity = (PresentationActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_finish, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean allDone = presentationActivity.allSettingsAndPermissionsAreReady();

        imageView.setImageResource(allDone ? R.drawable.ic_done_midnight_blue_48dp : R.drawable.ic_error_outline_midnight_blue_48dp);
        description.setText(allDone ? R.string.all_settings_and_permissions_are_ready_message : R.string.all_settings_and_permissions_are_not_ready_message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
