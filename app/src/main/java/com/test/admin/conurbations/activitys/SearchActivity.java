package com.test.admin.conurbations.activitys;

import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivitySearchBinding;
import com.test.admin.conurbations.fragments.SearchFragment;

import java.util.Random;

/**
 * Created by zhouqiong on 2015/11/3 0003.
 */

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    SearchFragment mSearchFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData(Bundle bundle) {

        initToolbar(mBinding.toolbarSearchToolbar, "", "");
        initAppBarSetting();
        String mSearchQuery = getIntent().getStringExtra(SearchFragment.CLASS_SEARCH);
        mBinding.ctlSearchHead.setTitle(mSearchQuery);

        Picasso.with(this).load(getBgImg()).into(mBinding.ivSearchHeadBg);
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SearchFragment.CLASS_SEARCH, mSearchQuery);
            mSearchFragment = new SearchFragment();
            mSearchFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rl_search_container, mSearchFragment)
                    .commit();
        }

        mBinding.fabSearchView.setOnClickListener(v -> clickFab());
    }

    public void initAppBarSetting() {
        mBinding.ablSearchHand.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i == 0) {
                mBinding.fabSearchView.hide();
            } else {
                mBinding.fabSearchView.show();
            }
        });
    }

    public void clickFab() {
        mSearchFragment.getRecyclerView().scrollToPosition(0);
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
