package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class RequestFingerprintFragment extends Fragment {

    @Bind(R.id.header)
    AppCompatTextView header;
    @Bind(R.id.imageView)
    AppCompatImageView imageView;
    @Bind(R.id.description)
    AppCompatTextView description;

    public RequestFingerprintFragment() {
        // Required empty public constructor
    }

    public static RequestFingerprintFragment newInstance() {
        return new RequestFingerprintFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_fingerprint, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateMessage(int text) {
        description.setText(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
