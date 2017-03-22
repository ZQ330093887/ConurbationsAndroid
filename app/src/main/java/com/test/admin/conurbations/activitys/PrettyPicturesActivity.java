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
import com.test.admin.conurbations.fragments.PrettyPicturesFragment;

import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wenhuaijun on 2015/11/3 0003.
 */

public class PrettyPicturesActivity extends BaseActivity {
    @Bind(R.id.toolbar_pretty_pictures_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.iv_pretty_pictures_head_bg)
    ImageView mHeadBgImageView;
    @Bind(R.id.ctl_pretty_pictures_head)
    CollapsingToolbarLayout mHeadToolbarLayout;
    @Bind(R.id.abl_pretty_pictures_hand)
    AppBarLayout mHeadAppBarLayout;
    @Bind(R.id.fab_pretty_pictures_fab)
    FloatingActionButton mFabFloatingActionButton;
    PrettyPicturesFragment mPrettyPicturesItemFragment;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_pretty_pictures;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbar,"","");
        initAppBarSetting();

        mHeadToolbarLayout.setTitle(getIntent().getStringExtra(PrettyPicturesFragment.CLASS_TITLE));

        Picasso.with(this).load(getBgImg()).into(mHeadBgImageView);
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PrettyPicturesFragment.CLASS_ID, getIntent().getStringExtra(PrettyPicturesFragment.CLASS_ID));
            mPrettyPicturesItemFragment = new PrettyPicturesFragment();
            mPrettyPicturesItemFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rl_pretty_pictures_container, mPrettyPicturesItemFragment)
                    .commit();
        }
    }

    @Override
    protected void initPresenter() {

    }

    public void initAppBarSetting() {
        mHeadAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    mFabFloatingActionButton.hide();
                } else {
                    mFabFloatingActionButton.show();
                }
            }
        });
    }

    @OnClick(R.id.fab_pretty_pictures_fab)
    public void clickFab(View view) {
        mPrettyPicturesItemFragment.getRecyclerView().setSelection(0);
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
