package com.test.admin.conurbations.activitys;

import android.os.Bundle;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.NewsDetailFragment;


/**
 * Created by laucherish on 16/3/17.
 */
public class NewsDetailActivity extends BaseActivity {

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(NewsDetailFragment.KEY_NEWS, getIntent().getIntExtra(NewsDetailFragment.KEY_NEWS, 0));
            NewsDetailFragment fragment = new NewsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void initPresenter() {

    }
}
