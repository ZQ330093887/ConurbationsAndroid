package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.activitys.INewsInfoDetailListView;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.presenter.NewsInfoListDetailPresenter;
import com.test.admin.conurbations.utils.HtmlUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhouqiong on 16/3/17.
 */
public class NewsInfoListDetailFragment extends BaseFragment implements INewsInfoDetailListView {

    @FindView
    ImageView mHeadImageView;
    @FindView
    TextView mTitleTextView;
    @FindView
    TextView mSourceTextView;
    @FindView
    TextView mNewsTextView;
    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    CollapsingToolbarLayout mHeadCollapsingToolbarLayout;
    @FindView
    WebView mNewsWebView;
    @FindView
    TextView mEmptyTextView;
    @FindView
    TextView mErrorTextView;
    private int mID;
    private String keyTitle;
    private String keyNbaIndex;
    private NewsInfoListDetailPresenter mNewsInfoListDetailPresenter;
    public static final String KEY_NEWS = "key_news";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_NBA_INDEX = "key_nba_index";


    @Override
    public void setNewsInfoDetailData(NewsDetail newsDetail) {
        if (newsDetail == null) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(keyNbaIndex)) {
                mNewsWebView.setVisibility(View.GONE);
                mNewsTextView.setVisibility(View.VISIBLE);
                mSourceTextView.setText(newsDetail.time);
                mTitleTextView.setText(newsDetail.title);
                List<Map<String, String>> content = newsDetail.content;
                for (Map<String, String> map : content) {
                    Set<String> set = map.keySet();
                    if (set.contains("img")) {
                        final String url = map.get("img");
                        Glide.with(getActivity())
                                .load(url)
                                .into(mHeadImageView);
                    } else {
                        if (!TextUtils.isEmpty(map.get("text"))) {
                            mNewsTextView.append(map.get("text") + "\n\n");
                        }
                    }
                }
                mEmptyTextView.setVisibility(View.GONE);
            } else {
                Glide.with(getActivity())
                        .load(newsDetail.getImage())
                        .centerCrop()
                        .into(mHeadImageView);
                mTitleTextView.setText(newsDetail.getTitle());
                mSourceTextView.setText(newsDetail.getImage_source());
                mNewsWebView.setDrawingCacheEnabled(true);
                String htmlData = HtmlUtil.createHtmlData(newsDetail, false);
                mNewsWebView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                mEmptyTextView.setVisibility(View.GONE);
            }
        }
        mErrorTextView.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle bundle) {
        if (getArguments().containsKey(KEY_NEWS)) {
            mID = getArguments().getInt(KEY_NEWS);
        }
        if (getArguments().containsKey(KEY_NEWS)) {
            keyTitle = getArguments().getString(KEY_TITLE);
        }
        if (getArguments().containsKey(KEY_NBA_INDEX)) {
            keyNbaIndex = getArguments().getString(KEY_NBA_INDEX);
        }
        mNewsInfoListDetailPresenter = new NewsInfoListDetailPresenter(this);
        setHasOptionsMenu(true);
        if (!TextUtils.isEmpty(keyTitle)) {
            initToolbar(mToolbarToolbar, keyTitle, "");
        } else {
            initToolbar(mToolbarToolbar, "新闻资讯", "");
        }
        mHeadCollapsingToolbarLayout.setTitleEnabled(true);
        loadData();
    }

    private void loadData() {
        if (!TextUtils.isEmpty(keyTitle)) {
            mNewsInfoListDetailPresenter.getNBANewsInfoDetailData(keyNbaIndex);
        } else {
            mNewsInfoListDetailPresenter.getNewsInfoDetailData(mID);
        }
    }

    @Override
    public void detachView() {
        if (mNewsInfoListDetailPresenter != null) {
            mNewsInfoListDetailPresenter.detachView();
        }
    }
}
