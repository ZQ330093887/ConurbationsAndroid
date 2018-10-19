package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.GanHuoDataBean;

import javax.inject.Inject;


/**
 * Created by zhouqiong on 2016/12/30.
 */

public class WelfareListAdapter extends BaseListAdapter<GanHuoDataBean> {

    @Inject
    public WelfareListAdapter(Fragment context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final GanHuoDataBean item) {
        vh.setImageUrlUserGlide(R.id.rv_item_welfare_photo, item.getUrl(), 0.618f, R.color.white)
                .setOnClickListener(R.id.rv_item_welfare_photo, v ->
                        startShowImageActivity(v, getStringToList(item.getUrl()))
                );
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
