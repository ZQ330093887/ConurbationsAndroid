package com.test.admin.conurbations.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.SoGouSearcher;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class SouGouImageAdapter extends BaseListAdapter<SoGouSearcher> {

    @Inject
    public SouGouImageAdapter(Fragment context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final SoGouSearcher item) {
        holder.setImageUrlUserGlide(R.id.rv_item_sougou_photo, item.getPic_url(), 0.618f, R.color.white)
                .setOnClickListener(R.id.rv_item_sougou_photo, v ->
                        startShowImageActivity(v, getStringToList(item.getPic_url()))
                );
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
