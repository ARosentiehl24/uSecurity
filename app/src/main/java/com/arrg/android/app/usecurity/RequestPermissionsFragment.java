package com.arrg.android.app.usecurity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestPermissionsFragment extends Fragment {

    @Bind(R.id.imageView)
    AppCompatImageView imageView;
    @Bind(R.id.header)
    AppCompatTextView header;
    @Bind(R.id.description)
    AppCompatTextView description;
    private int resIdHeader;
    private int resIdImage;
    private int resIdDescription;

    public RequestPermissionsFragment() {

    }

    public static RequestPermissionsFragment newInstance(int resIdHeader, int resIdImage, int resIdDescription) {
        RequestPermissionsFragment requestPermissionsFragment = new RequestPermissionsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("header", resIdHeader);
        bundle.putInt("image", resIdImage);
        bundle.putInt("description", resIdDescription);

        requestPermissionsFragment.setArguments(bundle);

        return requestPermissionsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        this.resIdHeader = bundle.getInt("header");
        this.resIdImage = bundle.getInt("image");
        this.resIdDescription = bundle.getInt("description");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_permissions, container, false);
        ButterKnife.bind(this, view);
        TypefaceHelper.typeface(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        header.setText(resIdHeader);
        imageView.setImageResource(resIdImage);
        description.setText(resIdDescription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
