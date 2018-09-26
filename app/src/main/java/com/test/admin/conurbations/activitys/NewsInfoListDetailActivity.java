package com.test.admin.conurbations.activitys;

import android.os.Bundle;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityNewsInfoListDetailBinding;
import com.test.admin.conurbations.fragments.NewsInfoListDetailFragment;


/**
 * Created by zhouqiong on 17/3/17.
 */
public class NewsInfoListDetailActivity extends BaseActivity<ActivityNewsInfoListDetailBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_info_list_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(NewsInfoListDetailFragment.KEY_NEWS, getIntent().getIntExtra(NewsInfoListDetailFragment.KEY_NEWS, 0));
            arguments.putString(NewsInfoListDetailFragment.KEY_NBA_INDEX, getIntent().getStringExtra(NewsInfoListDetailFragment.KEY_NBA_INDEX));
            arguments.putString(NewsInfoListDetailFragment.KEY_TITLE, getIntent().getStringExtra(NewsInfoListDetailFragment.KEY_TITLE));
            NewsInfoListDetailFragment fragment = new NewsInfoListDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_news_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void initPresenter() {

    }
}
