package com.test.admin.conurbations.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.ContentFragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by zhouqiong on 2015/9/23.
 */
public class FragmentHelp extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.tabLayout_content)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager_content)
    ViewPager mViewPager;
    @Bind(R.id.fabBtn_content)
    FloatingActionButton fabBtn;

    @Override
    public BaseFragment newInstance() {
        return new FragmentHelp ();
    }

    private ContentFragmentPagerAdapter contentFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment, container, false);
        ButterKnife.bind(this, view);
        int content = getArguments().getInt("content");
        contentFragmentPagerAdapter = new ContentFragmentPagerAdapter(getChildFragmentManager());
        mTabLayout.setTabsFromPagerAdapter(contentFragmentPagerAdapter);
        mViewPager.setAdapter(contentFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setBackgroundColor(content);
        fabBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabBtn_content:
                Snackbar.make(v, "点击蓝色按钮", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Ok按钮被点击", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
