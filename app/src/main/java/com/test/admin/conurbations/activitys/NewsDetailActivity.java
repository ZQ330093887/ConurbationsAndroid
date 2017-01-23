package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.NewsDetailFragment;


/**
 * Created by laucherish on 16/3/17.
 */
public class NewsDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(NewsDetailFragment.KEY_NEWS, getIntent().getIntExtra(NewsDetailFragment.KEY_NEWS, 0));
            NewsDetailFragment fragment = new NewsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, fragment)
                    .commit();
        }
    }
}
