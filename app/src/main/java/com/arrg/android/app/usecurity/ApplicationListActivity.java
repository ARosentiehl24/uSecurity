package com.arrg.android.app.usecurity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jaouan.revealator.Revealator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplicationListActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.initialView)
    View initialView;
    @Bind(R.id.btnBack)
    AppCompatImageButton btnBack;
    @Bind(R.id.searchInput)
    AppCompatEditText searchInput;
    @Bind(R.id.revealView)
    LinearLayout revealView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
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
                        .withRevealDuration(50)
                        .withChildAnimationDuration(50)
                        .withTranslateDuration(50)
                        .withChildsAnimation()
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.background_light_dark));
                            }
                        })
                        .start();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnBack)
    public void onClick() {
        Revealator.unreveal(revealView)
                .withDuration(50)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    }
                })
                .start();
    }
}
