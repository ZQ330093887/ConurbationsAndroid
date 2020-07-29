package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentMusicMvBinding;


/**
 * 功能：在线排行榜
 * Created by zhouqiong on 2016/9/23.
 */
public class ChartsFragment extends BaseFragment<FragmentMusicMvBinding> {

    private static final String TAG = "BaiduPlaylistFragment";

    public static ChartsFragment newInstance() {
        Bundle args = new Bundle();
        ChartsFragment fragment = new ChartsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_music_mv;
    }

    @Override
    protected void initData(Bundle bundle) {
        String[] mTitles = getActivity().getResources().getStringArray(R.array.music_mv);

        String[] mTitleType = new String[]{Constants.BAIDU}; //, Constants.QQ, Constants.NETEASE
        Fragment[] mFragments = new Fragment[mTitleType.length];

        for (int i = 0; i < mTitleType.length; i++) {
            mFragments[i] = new NetPlayListFragment();
            ((NetPlayListFragment) mFragments[i]).setTable(mTitles[i], mTitleType[i]);
        }

        FragmentAdapter mInformationFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpNewsInformationContent.setAdapter(mInformationFragmentPagerAdapter);

        mBinding.get().tlNewsInformationHead.setVisibility(View.GONE);

//        mBinding.get().vpNewsInformationContent.setOffscreenPageLimit(3);

        mBinding.get().tlNewsInformationHead.setupWithViewPager(mBinding.get().vpNewsInformationContent);
    }

    @Override
    public void detachView() {

    }
}