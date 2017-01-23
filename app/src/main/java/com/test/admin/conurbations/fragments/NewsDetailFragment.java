package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.INewsDetaliListView;
import com.test.admin.conurbations.model.NewsDetail;
import com.test.admin.conurbations.presenter.NewsDetailListPresenter;
import com.test.admin.conurbations.utils.HtmlUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by laucherish on 16/3/17.
 */
public class NewsDetailFragment extends Fragment implements INewsDetaliListView {

    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_source)
    TextView mTvSource;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.wv_news)
    WebView mWvNews;
    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;
    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;
    private int mID;
    private NewsDetailListPresenter listPresenter;
    public static final String KEY_NEWS = "key_news";

    @Override
    public void setSearchData(NewsDetail newsDetail) {
        if (newsDetail == null) {
            mTvLoadEmpty.setVisibility(View.VISIBLE);
        } else {
            Glide.with(getActivity())
                    .load(newsDetail.getImage())
                    .into(mIvHeader);
            mTvTitle.setText(newsDetail.getTitle());
            mTvSource.setText(newsDetail.getImage_source());
            mWvNews.setDrawingCacheEnabled(true);
            String htmlData = HtmlUtil.createHtmlData(newsDetail, false);
            mWvNews.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
            mTvLoadEmpty.setVisibility(View.GONE);
        }
        mTvLoadError.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_news_detail, container, false);
        ButterKnife.bind(this, contentView);
        InitView();
        return contentView;
    }

    private void InitView() {
        if (getArguments().containsKey(KEY_NEWS)) {
            mID = getArguments().getInt(KEY_NEWS);
        }
        listPresenter = new NewsDetailListPresenter(this);
        setHasOptionsMenu(true);
        init();
        loadData();
    }

    private void init() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitleEnabled(true);
    }

    private void loadData() {
        listPresenter.getNewsDetail(mID);
    }

}
