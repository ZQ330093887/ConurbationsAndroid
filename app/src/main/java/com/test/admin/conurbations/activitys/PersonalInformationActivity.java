package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.PersonalInformationAdapter;
import com.test.admin.conurbations.listeners.AppBarStateChangeListener;
import com.test.admin.conurbations.views.CircleImageView;

import butterknife.Bind;


/**
 * Created by zhouqiong on 2016/11/29.
 */

public class PersonalInformationActivity extends BaseActivity {

    private PersonalInformationAdapter mInformationAdapter;
    @Bind(R.id.toolbar_personal_information_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rv_personal_information_content)
    RecyclerView mContentRecyclerView;
    @Bind(R.id.ctl_personal_information_head)
    CollapsingToolbarLayout mHeadCollapsingToolbarLayout;
    @Bind(R.id.abl_personal_information_head)
    AppBarLayout mHeadAppBarLayout;
    @Bind(R.id.iv_personal_information_hand)
    ImageView mHeadImageView;
    @Bind(R.id.civ_personal_information_title_photo)
    ImageView mPhotoImageView;
    @Bind(R.id.tv_personal_information_title)
    TextView mTitleTextView;
    @Bind(R.id.civ_personal_information_img)
    CircleImageView mImageCircleImageView;

    Bitmap bitmap;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbar, "", "");
        Intent intent = getIntent();

        if (intent != null) {
            bitmap = intent.getParcelableExtra("mPhotoBundle");
            mImageCircleImageView.setImageBitmap(bitmap);
            mPhotoImageView.setImageBitmap(bitmap);
            mHeadImageView.setImageBitmap(bitmap);
        } else {
            mImageCircleImageView.setBackgroundResource(R.color.white);
            mPhotoImageView.setBackgroundResource(R.color.white);
            mHeadImageView.setBackgroundResource(R.color.white);
        }

        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mInformationAdapter = new PersonalInformationAdapter(this);
        // 为mRecyclerView设置适配器
        mContentRecyclerView.setAdapter(mInformationAdapter);
        mHeadAppBarLayout.addOnOffsetChangedListener(mStateChangeListener);
    }

    @Override
    protected void initPresenter() {
    }

    AppBarStateChangeListener mStateChangeListener = new AppBarStateChangeListener() {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            if (state == State.EXPANDED) {
                //展开状态
                mPhotoImageView.setVisibility(View.GONE);
                mImageCircleImageView.setVisibility(View.VISIBLE);
                mTitleTextView.setText("");
                mHeadCollapsingToolbarLayout.setTitle("个人信息");
            } else if (state == State.COLLAPSED) {
                //折叠状态
                mPhotoImageView.setVisibility(View.VISIBLE);
                mImageCircleImageView.setVisibility(View.GONE);
                mTitleTextView.setText(R.string.guard_msg);

            } else {
                //中间状态
                mPhotoImageView.setVisibility(View.GONE);
                mImageCircleImageView.setVisibility(View.VISIBLE);
                mTitleTextView.setText("");
            }
        }
    };
}
