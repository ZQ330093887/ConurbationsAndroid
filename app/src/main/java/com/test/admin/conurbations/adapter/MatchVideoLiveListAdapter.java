package com.test.admin.conurbations.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.VideoLiveInfo;
import com.test.admin.conurbations.presenter.MatchVideoLivePresenter;
import com.test.admin.conurbations.utils.RatioImageView;

public class MatchVideoLiveListAdapter extends BaseListAdapter<VideoLiveInfo> {

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final VideoLiveInfo info) {
        RatioImageView mLeftRatioImageView = vh.getView(R.id.iv_left_leam);
        RatioImageView mRightRatioImageView = vh.getView(R.id.iv_right_team);
        mLeftRatioImageView.setRatio(0.918f);
        mRightRatioImageView.setRatio(0.918f);
        Glide.with(mLeftRatioImageView.getContext())
                .load(info.leftImg)
                .centerCrop()
                .placeholder(R.mipmap.nba_default)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mLeftRatioImageView);

        Glide.with(mRightRatioImageView.getContext())
                .load(info.rightImg)
                .centerCrop()
                .placeholder(R.mipmap.nba_default)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mRightRatioImageView);

        vh.setText(R.id.tv_left_leam, info.leftName)
                .setText(R.id.tv_right_team, info.rightName)
                .setText(R.id.tv_live_time, info.time)
                .setText(R.id.tv_live_type, info.type);
        vh.setOnClickListener(R.id.rl_match_video_live, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchVideoLivePresenter.getVideoLiveSourceInfo(info.link, v.getContext());
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new MatchVideoLiveHolder(inflateItemView(parent, R.layout.item_match_video_list));
    }

    class MatchVideoLiveHolder extends BaseViewHolder {
        public MatchVideoLiveHolder(View itemView) {
            super(itemView);
        }
    }
}
