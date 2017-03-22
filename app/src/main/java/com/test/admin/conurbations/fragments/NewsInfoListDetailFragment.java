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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.INewsInfoDetailListView;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.presenter.NewsInfoListDetailPresenter;
import com.test.admin.conurbations.utils.HtmlUtil;
import com.test.admin.conurbations.utils.RatioImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 16/3/17.
 */
public class NewsInfoListDetailFragment extends Fragment implements INewsInfoDetailListView {

    @Bind(R.id.rv_news_info_list_detail_header)
    RatioImageView mHeadRatioImageView;
    @Bind(R.id.tv_news_info_list_detail_title)
    TextView mTitleTextView;
    @Bind(R.id.tv_news_info_list_detail_source)
    TextView mSourceTextView;
    @Bind(R.id.toolbar_news_detail_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_news_info_list_detail_head)
    CollapsingToolbarLayout mHeadCollapsingToolbarLayout;
    @Bind(R.id.wv_news_info_list_detail_news)
    WebView mNewsWebView;
    @Bind(R.id.tv_load_empty)
    TextView mEmptyTextView;
    @Bind(R.id.tv_load_error)
    TextView mErrorTextView;
    private int mID;
    private NewsInfoListDetailPresenter mNewsInfoListDetailPresenter;
    public static final String KEY_NEWS = "key_news";

    @Override
    public void setNewsInfoDetailData(NewsDetail newsDetail) {
        if (newsDetail == null) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mHeadRatioImageView.setRatio(0.918f);
            Glide.with(getActivity())
                    .load(newsDetail.getImage())
                    .into(mHeadRatioImageView);
            mTitleTextView.setText(newsDetail.getTitle());
            mSourceTextView.setText(newsDetail.getImage_source());
            mNewsWebView.setDrawingCacheEnabled(true);
            String htmlData = HtmlUtil.createHtmlData(newsDetail, false);
            mNewsWebView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
            mEmptyTextView.setVisibility(View.GONE);
        }
        mErrorTextView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_news_info_list_detail, container, false);
        ButterKnife.bind(this, contentView);
        InitView();
        return contentView;
    }

    private void InitView() {
        if (getArguments().containsKey(KEY_NEWS)) {
            mID = getArguments().getInt(KEY_NEWS);
        }
        mNewsInfoListDetailPresenter = new NewsInfoListDetailPresenter(this);
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mHeadCollapsingToolbarLayout.setTitleEnabled(true);
    }

    private void loadData() {
        mNewsInfoListDetailPresenter.getNewsInfoDetailData(mID);
    }

}
