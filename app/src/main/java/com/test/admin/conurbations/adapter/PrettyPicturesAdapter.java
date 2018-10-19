package com.test.admin.conurbations.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.TSZImageBean;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2016/2/7 0007.
 */
public class PrettyPicturesAdapter extends BaseListAdapter<TSZImageBean> {
    @Inject
    public PrettyPicturesAdapter(Fragment context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final TSZImageBean item) {

        vh.setImageUrlUserGlide(R.id.rv_item_sougou_photo, item.getUrl(), 0.918f, R.color.white)
                .setOnClickListener(R.id.rv_item_sougou_photo, v -> {
                    startShowImageActivity(v, getStringToList(item.getUrl()));
                });
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
}
