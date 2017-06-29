package com.test.admin.conurbations.adapter;

import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.SosoSearcher;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class SouGouImageAdapter extends BaseListAdapter<SosoSearcher> {

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final SosoSearcher item) {
        holder.setImageUrlUserGlide(R.id.rv_item_sougou_photo, item.getPic_url(), 0.618f, R.color.white)
                .setOnClickListener(R.id.rv_item_sougou_photo, v -> startShowImageActivity(v, item.getPic_url()));
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
}
