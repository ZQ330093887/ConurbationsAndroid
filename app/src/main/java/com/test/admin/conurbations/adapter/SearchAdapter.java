package com.test.admin.conurbations.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.SosoSearcher;

/**
 * Created by wenhuaijun on 2016/2/7 0007.
 */
public class SearchAdapter extends BaseListAdapter<SosoSearcher> {
    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final SosoSearcher item) {
        vh.setImageUrlUserGlide(R.id.rv_item_sougou_photo, item.getPic_url(), 0.918f, R.color.white)
                .setOnClickListener(R.id.rv_item_sougou_photo, v ->
                        //
                        startShowImageActivity(v, getStringToList(item.getPic_url()))
                );
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
