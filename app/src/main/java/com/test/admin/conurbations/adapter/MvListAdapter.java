package com.test.admin.conurbations.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.MvDetailActivity;
import com.test.admin.conurbations.config.Constants;
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

        vh.setTag(R.id.frameLayout, detail);
        vh.setOnClickListener(R.id.frameLayout, this::onClickListener);
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolder(inflateItemView(parent, R.layout.item_mv_list));
    }

    private class SearchHolder extends BaseViewHolder {
        private SearchHolder(View itemView) {
            super(itemView);
        }
    }


    private void onClickListener(View view) {
        MvInfo.MvInfoDetail detail = (MvInfo.MvInfoDetail) view.getTag();
        Intent intent = new Intent(view.getContext(), MvDetailActivity.class);
        intent.putExtra(Constants.MV_TITLE, detail.name);
        intent.putExtra(Constants.MV_ID, String.valueOf(detail.id));
        view.getContext().startActivity(intent);
    }
}
