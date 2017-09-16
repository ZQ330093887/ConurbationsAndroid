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
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.annotations.events.OnClick;
import com.test.admin.conurbations.fragments.SearchFragment;

import java.util.Random;

/**
 * Created by zhouqiong on 2015/11/3 0003.
 */

public class SearchActivity extends BaseActivity {
    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    ImageView mHeadBgImageView;
    @FindView
    CollapsingToolbarLayout mHeadCollapsingToolbarLayout;
    @FindView
    AppBarLayout mHandAppBarLayout;
    @FindView
    FloatingActionButton mViewFloatingActionButton;
    SearchFragment mSearchFragment;

    @Override
    protected void initData(Bundle bundle) {

        initToolbar(mToolbarToolbar, "", "");
        initAppBarSetting();
        String mSearchQuery = getIntent().getStringExtra(SearchFragment.CLASS_SEARCH);
        mHeadCollapsingToolbarLayout.setTitle(mSearchQuery);

        Picasso.with(this).load(getBgImg()).into(mHeadBgImageView);
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SearchFragment.CLASS_SEARCH, mSearchQuery);
            mSearchFragment = new SearchFragment();
            mSearchFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rl_search_container, mSearchFragment)
                    .commit();
        }
    }

    @Override
    protected void initPresenter() {

    }

    public void initAppBarSetting() {
        mHandAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i == 0) {
                mViewFloatingActionButton.hide();
            } else {
                mViewFloatingActionButton.show();
            }
        });
    }

    @OnClick("mViewFloatingActionButton")
    public void clickFab(View view) {
        mSearchFragment.getRecyclerView().setSelection(0);
    }

    private int[] bgImages = {
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
        return bgImages[new Random().nextInt(bgImages.length)];
    }
}
