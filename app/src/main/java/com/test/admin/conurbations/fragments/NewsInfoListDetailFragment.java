package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.INewsInfoDetailListView;
import com.test.admin.conurbations.databinding.FragmentNewsInfoListDetailBinding;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.presenter.NewsInfoListDetailPresenter;
import com.test.admin.conurbations.utils.HtmlUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhouqiong on 16/3/17.
 */
public class NewsInfoListDetailFragment extends BaseFragment<FragmentNewsInfoListDetailBinding>
        implements INewsInfoDetailListView {

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
            mBinding.get().tvNewsInfoListDetailEmpty.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(keyNbaIndex)) {
                mBinding.get().wvNewsInfoListDetailNews.setVisibility(View.GONE);
                mBinding.get().tvNewsInfoListDetailNews.setVisibility(View.VISIBLE);
                mBinding.get().tvNewsInfoListDetailSource.setText(newsDetail.time);
                mBinding.get().tvNewsInfoListDetailTitle.setText(newsDetail.title);
                List<Map<String, String>> content = newsDetail.content;
                for (Map<String, String> map : content) {
                    Set<String> set = map.keySet();
                    if (set.contains("img")) {
                        final String url = map.get("img");
                        Glide.with(getActivity())
                                .load(url)
                                .into(mBinding.get().ivNewsInfoListDetailHead);
                    } else {
                        if (!TextUtils.isEmpty(map.get("text"))) {
                            mBinding.get().tvNewsInfoListDetailNews.append(map.get("text") + "\n\n");
                        }
                    }
                }
                mBinding.get().tvNewsInfoListDetailEmpty.setVisibility(View.GONE);
            } else {
                Glide.with(getActivity())
                        .load(newsDetail.getImage())
                        .centerCrop()
                        .into(mBinding.get().ivNewsInfoListDetailHead);
                mBinding.get().tvNewsInfoListDetailTitle.setText(newsDetail.getTitle());
                mBinding.get().tvNewsInfoListDetailSource.setText(newsDetail.getImage_source());
                mBinding.get().wvNewsInfoListDetailNews.setDrawingCacheEnabled(true);
                String htmlData = HtmlUtil.createHtmlData(newsDetail, false);
                mBinding.get().wvNewsInfoListDetailNews.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                mBinding.get().tvNewsInfoListDetailEmpty.setVisibility(View.GONE);
            }
        }
        mBinding.get().tvNewsInfoListDetailError.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_info_list_detail;
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
            initToolbar(mBinding.get().toolbarNewsInfoListDetailToolbar, keyTitle, "");
        } else {
            initToolbar(mBinding.get().toolbarNewsInfoListDetailToolbar, "新闻资讯", "");
        }
        mBinding.get().ctlNewsInfoListDetailHead.setTitleEnabled(true);
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
