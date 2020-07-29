package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.MvDetailAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.databinding.ActivityMvDetailBinding;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.presenter.MvDetailPresenter;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.StatusBarUtil;
import com.test.admin.conurbations.widget.statuslayoutmanage.OnStatusChildClickListener;
import com.test.admin.conurbations.widget.statuslayoutmanage.StatusLayoutManager;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by zhouqiong on 2015/11/3 0003.
 */

public class MvDetailActivity extends BaseActivity<ActivityMvDetailBinding> implements IMvDetail {

    private MvDetailPresenter mvDetailPresenter;
    private MvDetailAdapter mvDetailAdapter;
    private StatusLayoutManager statusLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mv_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mBinding.toolbar);
        StatusBarUtil.darkMode(this, false);
        initEmptyView();
        initView();

        loadingData();
    }

    //初始化空View
    private void initEmptyView() {
        if (statusLayoutManager == null) {
            statusLayoutManager = new StatusLayoutManager.Builder(mBinding.llLayout)
                    .setOnStatusChildClickListener(new OnStatusChildClickListener() {
                        @Override
                        public void onEmptyChildClick(View view) {
                        }

                        @Override
                        public void onErrorChildClick(View view) {
                            statusLayoutManager.showLoadingLayout();
                            loadingData();
                        }

                        @Override
                        public void onCustomerChildClick(View view) {
                        }
                    }).build();
        }
        statusLayoutManager.showLoadingLayout();
    }

    private void initView() {
        String title = getIntent().getStringExtra(Constants.MV_TITLE);
        initToolbar(mBinding.toolbar, title, "");

        mvDetailPresenter = new MvDetailPresenter(this);
        mvDetailAdapter = new MvDetailAdapter(this);
        mBinding.rvSimilarMv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvSimilarMv.setAdapter(mvDetailAdapter);
    }


    private void loadingData() {
        String mVid = getIntent().getStringExtra(Constants.MV_ID);
        mvDetailPresenter.loadMvDetail(mVid);
        mvDetailPresenter.loadSimilarMv(mVid);
    }


    @Override
    public void showMvList(List<MvInfo.MvInfoDetail> mvList) {
        mvDetailAdapter.setList(mvList);
        mvDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMvDetailInfo(MvInfo.MvInfoDetailInfo mvInfoDetailInfo) {
        if (mvInfoDetailInfo != null && mvInfoDetailInfo.brs.p720 != null) {
            mBinding.nestedScrollView.setVisibility(View.VISIBLE);
            String url = mvInfoDetailInfo.brs.p720;

            mBinding.videoView.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            SaveBitmapUtils.loadImageView(this, mvInfoDetailInfo.cover, mBinding.videoView.thumbImageView);

            mBinding.singerTv.setText(mvInfoDetailInfo.artistName);
        }
    }

    @Override
    public void showError(String message) {
        statusLayoutManager.showErrorLayout();
    }

    @Override
    public void showFinishState() {
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
