package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.norbsoft.typefacehelper.TypefaceHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestPatternFragment extends Fragment {

    @Bind(R.id.materialLockView)
    MaterialLockView materialLockView;

    private PresentationActivity presentationActivity;

    public RequestPatternFragment() {
        // Required empty public constructor
    }

    public static RequestPatternFragment newInstance() {
        return new RequestPatternFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presentationActivity = (PresentationActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_request_pattern, container, false);
        ButterKnife.bind(this, view);
        TypefaceHelper.typeface(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, final String SimplePattern) {
                super.onPatternDetected(pattern, SimplePattern);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (patternWasConfigured()) {
                                    if (materialLockView.isEnabled()) {
                                        if (userPattern().equals(SimplePattern)) {
                                            presentationActivity.updateText(PresentationActivity.PATTERN, R.string.pattern_configured_message);

                                            materialLockView.setEnabled(false);
                                        } else {
                                            presentationActivity.updateText(PresentationActivity.PATTERN, R.string.wrong_pattern_message);

                                            materialLockView.clearPattern();
                                        }
                                    }
                                } else {
                                    PreferencesManager.putString(getString(R.string.user_pattern), SimplePattern);

                                    presentationActivity.updateText(PresentationActivity.PATTERN, R.string.confirm_pattern_message);

                                    materialLockView.clearPattern();
                                }
                            }
                        });
                    }
                }, 250);
            }
        });
    }

    private boolean patternWasConfigured() {
        return userPattern().length() != 0;
    }

    private String userPattern() {
        return PreferencesManager.getString(getString(R.string.user_pattern), "");
    }

    public void resetPattern() {
        materialLockView.setEnabled(true);

        materialLockView.clearPattern();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        presentationActivity = null;
    }
}
