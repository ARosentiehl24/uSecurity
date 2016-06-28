package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RequestPinFragment extends SlideFragment {

    @Bind(R.id.pinLockView)
    PinLockView pinLockView;
    @Bind(R.id.indicatorDots)
    IndicatorDots indicatorDots;

    private PresentationActivity presentationActivity;

    public RequestPinFragment() {

    }

    public static RequestPinFragment newInstance() {
        return new RequestPinFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presentationActivity = (PresentationActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_pin, container, false);
        ButterKnife.bind(this, view);
        TypefaceHelper.typeface(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(final String pin) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (pinWasConfigured()) {
                                    if (pinLockView.isIndicatorDotsAttached()) {
                                        if (userPin().equals(pin)) {
                                            presentationActivity.updateText(PresentationActivity.PIN, R.string.pin_configured_message);

                                            pinLockView.attachIndicatorDots(null);
                                        } else {
                                            presentationActivity.updateText(PresentationActivity.PIN, R.string.wrong_pin_message);

                                            pinLockView.resetPinLockView();
                                        }
                                    }
                                } else {
                                    PreferencesManager.putString(getString(R.string.user_pin), pin);

                                    presentationActivity.updateText(PresentationActivity.PIN, R.string.confirm_pin_message);

                                    pinLockView.resetPinLockView();
                                }
                            }
                        });
                    }
                }, 250);
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public boolean pinWasConfigured() {
        return userPin().length() != 0;
    }

    public String userPin() {
        return PreferencesManager.getString(getString(R.string.user_pin), "");
    }

    public void resetPinView() {
        pinLockView.attachIndicatorDots(indicatorDots);

        pinLockView.resetPinLockView();
    }
}
