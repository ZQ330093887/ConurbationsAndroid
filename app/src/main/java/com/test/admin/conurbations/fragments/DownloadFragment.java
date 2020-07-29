package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.databinding.FragmentDownloadBinding;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class DownloadFragment extends BaseFragment<FragmentDownloadBinding> {

    public static DownloadFragment newInstance(Boolean isCache) {
        Bundle args = new Bundle();
        DownloadFragment fragment = new DownloadFragment();
        args.putBoolean("is_cache", isCache);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.get().toolbar, "下载缓存", "");


        String[] mTitles = getActivity().getResources().getStringArray(R.array.my_download);
        Fragment[] mFragments = new Fragment[mTitles.length];

        mFragments[0] = DownloadedFragment.newInstance(true);
        mFragments[1] = DownloadedFragment.newInstance(false);
        mFragments[2] = DownloadManagerFragment.newInstance();

        FragmentAdapter fa = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().mViewpager.setAdapter(fa);
        mBinding.get().tabs.setupWithViewPager(mBinding.get().mViewpager);
        mBinding.get().mViewpager.setOffscreenPageLimit(2);
        mBinding.get().mViewpager.setCurrentItem(0);
    }

    @Override
    public void detachView() {

    }
}
