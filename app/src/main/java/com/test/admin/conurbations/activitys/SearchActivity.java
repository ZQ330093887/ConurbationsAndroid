package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.SearchFragment;

import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wenhuaijun on 2015/11/3 0003.
 */

public class SearchActivity extends BaseActivity {
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
    protected int setLayoutResourceID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData(Bundle bundle) {

        initToolbar(toolbar, "", "");
        initAppBarSetting();
        String mSearchQuery = getIntent().getStringExtra(SearchFragment.CLASS_SEARCH);
        collapsingToolbarLayout.setTitle(mSearchQuery);

        Picasso.with(this).load(getBgImg()).into(imageView);
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SearchFragment.CLASS_SEARCH, mSearchQuery);
            fragment = new SearchFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void initPresenter() {

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

    private int[] bgImgs = {
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

    public int getBgImg() {
        return bgImgs[new Random().nextInt(bgImgs.length)];
    }
}
