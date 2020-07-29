package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.IDiscoverView;
import com.test.admin.conurbations.adapter.TopPlaylistAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentDiscoverBinding;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.BannerBean;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.presenter.DiscoverPresenter;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.widget.GlideImageLoader;
import com.youth.banner.BannerConfig;
import com.youth.banner.transformer.TabletTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现
 * Created by zhouqiong on 2016/9/23.
 */

public class DiscoverFragment extends BaseFragment<FragmentDiscoverBinding> implements IDiscoverView {
    private DiscoverPresenter mPresenter;
    private TopPlaylistAdapter mAdapter;
    private ArrayList<Artist> artistList;
    private ArrayList<NewsList> musicList;

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();
        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
        loadData();
        listener();
    }

    private void initView() {
        mPresenter = new DiscoverPresenter(this);
        mBinding.get().wangChartsRv.setLayoutManager(new GridLayoutManager(getContext(),
                2, LinearLayoutManager.VERTICAL, false));
        mBinding.get().wangChartsRv.setNestedScrollingEnabled(false);
        mAdapter = new TopPlaylistAdapter(getBaseActivity());
        mBinding.get().wangChartsRv.setAdapter(mAdapter);

    }

    private void loadData() {
        mPresenter.loadBannerView();
        mPresenter.loadNetease("全部");
        mPresenter.loadArtists();
        mPresenter.loadRaios();
    }

    @Override
    public void showBannerView(List<BannerBean> banners) {
        List<String> bannerImg = new ArrayList<>();
        for (BannerBean b : banners) {
            bannerImg.add(b.getPicUrl());
        }
        mBinding.get().mzBannerView
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
                .setBannerAnimation(TabletTransformer.class)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .update(bannerImg);
    }

    @Override
    public void showEmptyView(String msg) {
        ToastUtils.getInstance().showToast(msg);
    }

    @Override
    public void showNeteaseCharts(List<NewsList> charts) {
        mAdapter.setList(charts);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showArtistCharts(ArrayList<Artist> musicList) {
        this.artistList = musicList;
    }

    @Override
    public void showRadioChannels(ArrayList<NewsList> musicList) {
        this.musicList = musicList;
    }

    /**
     * 更新分类标签
     */
    private void updateCate(String name) {
        mBinding.get().cateTagTv.setText(name);
        mPresenter.loadNetease(name);
    }


    private void listener() {
        mBinding.get().catTag3Tv.setOnClickListener(this::onClick);
        mBinding.get().cateTagTv.setOnClickListener(this::onClick);
        mBinding.get().catTag1Tv.setOnClickListener(this::onClick);
        mBinding.get().catTag2Tv.setOnClickListener(this::onClick);
        mBinding.get().radioTv.setOnClickListener(this::onClick);
        mBinding.get().hotSingerTv.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cateTagTv) {
            toCatTagAll();
        } else if (id == R.id.catTag1Tv) {
            updateCate("华语");
        } else if (id == R.id.catTag2Tv) {
            updateCate("流行");
        } else if (id == R.id.catTag3Tv) {
            updateCate("古风");
        } else if (id == R.id.hotSingerTv) {
            NavigationHelper.navigateToAllList(getBaseActivity(), Constants.NETEASE_ARITIST_LIST, artistList, musicList, null);
        } else if (id == R.id.radioTv) {
            NavigationHelper.navigateToAllList(getBaseActivity(), Constants.BAIDU_RADIO_LIST, artistList, musicList, null);
        }
    }

    private void toCatTagAll() {
        AllCategoryFragment categoryFragment = new AllCategoryFragment();
        categoryFragment.setSuccessListener(s -> {
            updateCate(s);
            return null;
        });
        categoryFragment.showIt(getBaseActivity());

    }

    @Override
    public void detachView() {

    }

}
