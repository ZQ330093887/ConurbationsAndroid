package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.ShowImageActivity;
import com.test.admin.conurbations.model.entity.GanHuoDataBean;
import com.test.admin.conurbations.utils.RatioImageView;


/**
 * Created by zhouqiong on 2016/12/30.
 */

public class WelfareListAdapter extends BaseListAdapter<GanHuoDataBean> {

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final GanHuoDataBean item) {
        final RatioImageView mPhotoRatioImageView = vh.getView(R.id.rv_item_welfare_photo);
        mPhotoRatioImageView.setRatio(0.618f);
        mPhotoRatioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPictureActivity(view, item);
            }
        });
        Glide.with(mPhotoRatioImageView.getContext())
                .load(item.getUrl())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .into(mPhotoRatioImageView);
    }


    private void startPictureActivity(View transitView, GanHuoDataBean item) {
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

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SampleViewHolder(inflateItemView(parent, R.layout.item_welfare));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
