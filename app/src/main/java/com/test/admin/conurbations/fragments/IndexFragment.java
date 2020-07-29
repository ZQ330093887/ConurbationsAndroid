package com.test.admin.conurbations.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.FragmentIndexBinding;
import com.test.admin.conurbations.model.response.Moment;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class IndexFragment extends BaseFragment<FragmentIndexBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void initData(Bundle bundle) {
        RxBus.getDefault().post(new Event(R.color.theme_primary, Constants.STATUE_BAR_COLOR));

        String[] mTitles = getActivity().getResources().getStringArray(R.array.index_tab);
        String[] titleId = getActivity().getResources().getStringArray(R.array.index_tab_id);
        Fragment[] mFragments = new Fragment[mTitles.length];
        mFragments[0] = new GankHotFragment();
        ((GankHotFragment) mFragments[0]).setRange(Moment.Range.THREE);
        mFragments[1] = new WelfareFragment();
        ((WelfareFragment) mFragments[1]).setRange(Moment.Range.ONE);
        for (int i = 2; i < mTitles.length; i++) {
            mFragments[i] = new GanHuoFragment();
            ((GanHuoFragment) mFragments[i]).setRange(titleId[i]);
        }

        mBinding.get().vpIndexContent.setAdapter(new FragmentAdapter(getChildFragmentManager(), mTitles, mFragments));
        mBinding.get().vpIndexContent.setOffscreenPageLimit(9);
        mBinding.get().tlIndexHead.setupWithViewPager(mBinding.get().vpIndexContent);
        mBinding.get().tlIndexHead.setBackgroundColor(getArguments().getInt("content"));
    }

    @Override
    public void detachView() {
    }
}
