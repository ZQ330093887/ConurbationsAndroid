package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentPictureBinding;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class PictureFragment extends BaseFragment<FragmentPictureBinding> {

    private FragmentAdapter fragmentPrettyFragmentPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void initData(Bundle bundle) {
        initAppBarSetting();

        String[] mTitles = getActivity().getResources().getStringArray(R.array.sougou_tab);
        Fragment[] mFragments = new Fragment[mTitles.length];
        mFragments[0] = new PrettyPicturesListFragmentList();
        ((PrettyPicturesListFragmentList) mFragments[0]).setRange(Moment.SGImgType.美女);
        for (int i = 1; i < mTitles.length; i++) {
            mFragments[i] = new SouGouImageFragment();
            ((SouGouImageFragment) mFragments[i]).setRange(mTitles[i]);
        }

        fragmentPrettyFragmentPagerAdapter = new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments);
        mBinding.get().vpPictureContent.setAdapter(fragmentPrettyFragmentPagerAdapter);
        mBinding.get().vpPictureContent.setOffscreenPageLimit(5);
        mBinding.get().tlPictureHead.setupWithViewPager(mBinding.get().vpPictureContent);
        mBinding.get().tlPictureHead.setBackgroundColor(getArguments().getInt("content"));
    }

    public void initAppBarSetting() {
        RxBus.getDefault().post(new Event(R.color.colorTealPrimaryDark, Constants.STATUE_BAR_COLOR));
        mBinding.get().ablPictureHead.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i != 0) {
                mBinding.get().fabPictureView.hide();
            } else {
                mBinding.get().fabPictureView.show();
            }
        });

        mBinding.get().fabPictureView.setOnClickListener(v -> clickFab());
    }

    public void clickFab() {
        ((BaseSubFragment) fragmentPrettyFragmentPagerAdapter.getFragment(mBinding.get().vpPictureContent.getCurrentItem())).getRecyclerView().scrollToPosition(0);
    }

    @Override
    public void detachView() {
    }
}
