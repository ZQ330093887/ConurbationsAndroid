package com.test.admin.conurbations.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.ShowImageActivity;
import com.test.admin.conurbations.data.response.GanHuoDataBean;
import com.test.admin.conurbations.utils.RatioImageView;


/**
 * Created by zhouqiong on 2016/12/30.
 */

public class WelfareListAdapter extends BaseListAdapter<GanHuoDataBean> {

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final GanHuoDataBean item) {
        RatioImageView imageView = vh.getView(R.id.item_welfare_photo);
        imageView.setRatio(0.618f);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ShowImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("position", item.getUrl());
                if (bundle != null) {
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            }
        });
        Glide.with(imageView.getContext())
                .load(item.getUrl())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .into(imageView);
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
