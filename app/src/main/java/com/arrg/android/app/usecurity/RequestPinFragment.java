package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockView;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RequestPinFragment extends SlideFragment {


    @Bind(R.id.pinLockView)
    PinLockView pinLockView;
    @Bind(R.id.indicatorDots)
    IndicatorDots indicatorDots;

    public RequestPinFragment() {
    }

    public static RequestPinFragment newInstance() {
        return new RequestPinFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_pin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pinLockView.attachIndicatorDots(indicatorDots);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
