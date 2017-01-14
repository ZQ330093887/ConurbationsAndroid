package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.SearchFragment;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wenhuaijun on 2015/11/3 0003.
 */

public class SearchActivity extends AppCompatActivity {
    @Bind(R.id.search_toolbar)
    Toolbar toolbar;
    @Bind(R.id.bg_img)
    ImageView imageView;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.search_appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.search_fab)
    FloatingActionButton fab;
    SearchFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initAppBarSetting();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra(SearchFragment.CLASS_TITLE));
        imageView.setImageResource(getBgImg());
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SearchFragment.CLASS_ID, getIntent().getStringExtra(SearchFragment.CLASS_ID));
            fragment = new SearchFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_container, fragment)
                    .commit();
        }
    }

    public void initAppBarSetting() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }

    @OnClick(R.id.search_fab)
    public void clickFab(View view) {
        fragment.getRecyclerView().setSelection(0);
    }

    private int[] bgImgs ={
            R.drawable.bg_1,
            R.drawable.bg_2,
            R.drawable.bg_3,
            R.drawable.bg_4,
            R.drawable.bg_5,
            R.drawable.bg_6,
            R.drawable.bg_7,
            R.drawable.bg_8,
            R.drawable.bg_9,
    };
    public int getBgImg(){
        return bgImgs[new Random().nextInt(bgImgs.length)];
    }
}
