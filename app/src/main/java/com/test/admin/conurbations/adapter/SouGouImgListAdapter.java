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
import com.test.admin.conurbations.model.SosoSearcher;
import com.test.admin.conurbations.utils.RatioImageView;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class SouGouImgListAdapter extends BaseListAdapter<SosoSearcher> {

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final SosoSearcher item) {
        final RatioImageView imageView = holder.getView(R.id.item_sougou_photo);
        imageView.setRatio(0.618f);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPictureActivity(view, item);
            }
        });
        Glide.with(imageView.getContext())
                .load(item.getPic_url())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .into(imageView);
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_sg_img));
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
            ActivityCompat.startActivity((Activity) transitView.getContext(), intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            transitView.getContext().startActivity(intent);
        }
    }
}
