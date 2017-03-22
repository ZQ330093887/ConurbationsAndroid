package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.ShowImageActivity;
import com.test.admin.conurbations.model.entity.TSZImageBean;
import com.test.admin.conurbations.utils.RatioImageView;

/**
 * Created by wenhuaijun on 2016/2/7 0007.
 */
public class PrettyPicturesAdapter extends BaseListAdapter<TSZImageBean> {
    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final TSZImageBean item) {
        final RatioImageView mPhontRatioImageView = vh.getView(R.id.rv_item_sougou_photo);
        mPhontRatioImageView.setRatio(0.918f);
        mPhontRatioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPictureActivity(v,item);
            }
        });
        Glide.with(mPhontRatioImageView.getContext())
                .load(item.getUrl())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPhontRatioImageView);

    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolder(inflateItemView(parent, R.layout.item_sougou_image));
    }

    class SearchHolder extends BaseViewHolder {
        public SearchHolder(View itemView) {
            super(itemView);
        }
    }

    private void startPictureActivity(View transitView, TSZImageBean item) {
        Intent intent = ShowImageActivity.newIntent(transitView.getContext(), item.getUrl());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (Activity) transitView.getContext(), transitView, ShowImageActivity.TRANSIT_PIC);
        try {
            ActivityCompat.startActivity(transitView.getContext(), intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            transitView.getContext().startActivity(intent);
        }
    }
}
