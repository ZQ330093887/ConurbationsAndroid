package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.PersonalInformationAdapter;
import com.test.admin.conurbations.databinding.ActivityPersonalInformationBinding;
import com.test.admin.conurbations.listeners.AppBarStateChangeListener;


/**
 * Created by zhouqiong on 2016/11/29.
 */

public class PersonalInformationActivity extends BaseActivity<ActivityPersonalInformationBinding> {
    public final static String PHOTOBUNDLE = "phone_bundle";

    private PersonalInformationAdapter mInformationAdapter;
    Bitmap bitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.toolbarPersonalInformationToolbar, "", "");
        Intent intent = getIntent();

        if (intent != null) {
            bitmap = intent.getParcelableExtra(PHOTOBUNDLE);
            mBinding.civPersonalInformationImage.setImageBitmap(bitmap);
            mBinding.civPersonalInformationPhoto.setImageBitmap(bitmap);
            mBinding.ivPersonalInformationHead.setImageBitmap(bitmap);
        } else {
            mBinding.civPersonalInformationImage.setBackgroundResource(R.color.white);
            mBinding.civPersonalInformationPhoto.setBackgroundResource(R.color.white);
            mBinding.ivPersonalInformationHead.setBackgroundResource(R.color.white);
        }

        mBinding.rvPersonalInformationContent.setLayoutManager(new LinearLayoutManager(this));
        mInformationAdapter = new PersonalInformationAdapter(this);
        // 为mRecyclerView设置适配器
        mBinding.rvPersonalInformationContent.setAdapter(mInformationAdapter);
        mBinding.ablPersonalInformationHead.addOnOffsetChangedListener(mStateChangeListener);
    }

    AppBarStateChangeListener mStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            if (state == State.EXPANDED) {
                //展开状态
                mBinding.civPersonalInformationPhoto.setVisibility(View.GONE);
                mBinding.civPersonalInformationImage.setVisibility(View.VISIBLE);
                mBinding.tvPersonalInformationTitle.setText("");
                mBinding.ctlPersonalInformationHead.setTitle("个人信息");

            } else if (state == State.COLLAPSED) {
                //折叠状态
                mBinding.civPersonalInformationPhoto.setVisibility(View.VISIBLE);
                mBinding.civPersonalInformationImage.setVisibility(View.GONE);
                mBinding.tvPersonalInformationTitle.setText(R.string.guard_msg);

            } else {
                //中间状态
                mBinding.civPersonalInformationPhoto.setVisibility(View.GONE);
                mBinding.civPersonalInformationImage.setVisibility(View.VISIBLE);
                mBinding.tvPersonalInformationTitle.setText("");
            }
        }
    };
}
