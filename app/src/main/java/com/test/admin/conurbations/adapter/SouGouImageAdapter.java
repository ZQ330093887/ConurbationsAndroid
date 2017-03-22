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
import com.test.admin.conurbations.model.entity.SosoSearcher;
import com.test.admin.conurbations.utils.RatioImageView;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class SouGouImageAdapter extends BaseListAdapter<SosoSearcher> {

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final SosoSearcher item) {
        final RatioImageView mPhotoRatioImageView = holder.getView(R.id.rv_item_sougou_photo);
        mPhotoRatioImageView.setRatio(0.618f);
        mPhotoRatioImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPictureActivity(view, item);
            }
        });
        Glide.with(mPhotoRatioImageView.getContext())
                .load(item.getPic_url())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPhotoRatioImageView);
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_sougou_image));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(ViewGroup parent) {
            super(parent);
        }
    }

    private void startPictureActivity(View transitView, SosoSearcher item) {
        Intent intent = ShowImageActivity.newIntent(transitView.getContext(), item.getPic_url());
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
