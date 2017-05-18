package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.fragments.MatchVideoLiveListFragment;

/**
 * Created by zhouqiong on 2017/5/18.
 */

public class MatchVideoLiveListActivity extends BaseActivity {
    @FindView
    Toolbar mToolbarToolbar;
    MatchVideoLiveListFragment mMatchVideoLiveListFragment;

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, "直播列表", "");
        if (bundle == null) {
            mMatchVideoLiveListFragment = new MatchVideoLiveListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rl_match_video_live_list_container, mMatchVideoLiveListFragment)
                    .commit();
        }
    }

    @Override
    protected void initPresenter() {

    }
}
