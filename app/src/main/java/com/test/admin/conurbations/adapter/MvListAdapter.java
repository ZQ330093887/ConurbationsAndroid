package com.test.admin.conurbations.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.utils.CommonUtil;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2016/2/7 0007.
 */
public class MvListAdapter extends BaseListAdapter<MvInfo.MvInfoDetail> {
    @Inject
    public MvListAdapter(Fragment context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final MvInfo.MvInfoDetail detail) {

        vh.setText(R.id.tv_title, detail.name)
                .setText(R.id.tv_num, String.valueOf(vh.getAdapterPosition() + 1))
                .setText(R.id.tv_playCount, "播放次数：" +
                        CommonUtil.formatPlayCount(detail.playCount))
                .setImageUrlUserGlide(vh.getView(R.id.iv_cover), detail.cover, R.color.white);

        if (vh.getAdapterPosition() > 2) {
            vh.setTextColor(R.id.tv_num, Color.WHITE);
        } else {
            vh.setTextColor(R.id.tv_num, Color.RED);
        }
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolder(inflateItemView(parent, R.layout.item_mv_list));
    }

    class SearchHolder extends BaseViewHolder {
        public SearchHolder(View itemView) {
            super(itemView);
        }
    }
}
