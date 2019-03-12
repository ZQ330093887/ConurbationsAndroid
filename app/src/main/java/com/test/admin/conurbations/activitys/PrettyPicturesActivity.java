package com.test.admin.conurbations.activitys;

import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityPrettyPicturesBinding;
import com.test.admin.conurbations.fragments.PrettyPicturesFragment;
import com.test.admin.conurbations.utils.StatusBarUtil;

import java.util.Random;

/**
 * Created by zhouqiong on 2016/11/3.
 */

public class PrettyPicturesActivity extends BaseActivity<ActivityPrettyPicturesBinding> {

    PrettyPicturesFragment mPrettyPicturesItemFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pretty_pictures;
    }

    @Override
    protected void initData(Bundle bundle) {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mBinding.toolbarPrettyPicturesToolbar);
        StatusBarUtil.darkMode(this, false);
        initAppBarSetting();

        mBinding.ctlPrettyPicturesHead.setTitle(getIntent().getStringExtra(PrettyPicturesFragment.CLASS_TITLE));
        Picasso.with(this).load(getBgImg()).into(mBinding.ivPrettyPicturesHeadBg);
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PrettyPicturesFragment.CLASS_ID, getIntent().getStringExtra(PrettyPicturesFragment.CLASS_ID));
            mPrettyPicturesItemFragment = new PrettyPicturesFragment();
            mPrettyPicturesItemFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rl_pretty_pictures_container, mPrettyPicturesItemFragment)
                    .commit();
        }
        mBinding.fabPrettyPicturesFab.setOnClickListener(v -> clickFab());
    }

    public void initAppBarSetting() {
        mBinding.ablPrettyPicturesHead.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i == 0) {
                mBinding.fabPrettyPicturesFab.hide();
            } else {
                mBinding.fabPrettyPicturesFab.show();
            }
        });
    }

    public void clickFab() {
        mPrettyPicturesItemFragment.getRecyclerView().scrollToPosition(0);
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

    @Override
    public void detachView() {

    }
}
