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
import com.test.admin.conurbations.model.TSZImageBean;
import com.test.admin.conurbations.utils.RatioImageView;

/**
 * Created by wenhuaijun on 2016/2/7 0007.
 */
public class DayAndDayPrettyPicturesMoreAdapter extends BaseListAdapter<TSZImageBean> {
    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final TSZImageBean item) {
        final RatioImageView imageView = vh.getView(R.id.item_sougou_photo);
        imageView.setRatio(0.918f);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPictureActivity(v,item);
            }
        });
        Glide.with(imageView.getContext())
                .load(item.getUrl())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolder(inflateItemView(parent, R.layout.item_sg_img));
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
            ActivityCompat.startActivity((Activity) transitView.getContext(), intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            transitView.getContext().startActivity(intent);
        }
    }
}
