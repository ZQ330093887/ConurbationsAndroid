package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.MvDetailActivity;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.DateUtils;

/**
 * Created by ZQiong on 2019/1/15.
 */
public class MvDetailAdapter extends BaseListAdapter<MvInfo.MvInfoDetail> {


    public MvDetailAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final MvInfo.MvInfoDetail detail) {
        vh.setText(R.id.tv_title, detail.name)
                .setText(R.id.tv_play_count, CommonUtil.formatPlayCount(detail.playCount))
                .setText(R.id.tv_duration, DateUtils.formatTime(detail.duration))
                .setText(R.id.tv_author, "by " + detail.artistName)
                .setImageUrlUserGlide(vh.getView(R.id.iv_cover), detail.cover, R.mipmap.default_cover);

        vh.itemView.setTag(detail);
        vh.itemView.setOnClickListener(this::onClickListener);
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new MvDetailHolder(inflateItemView(parent, R.layout.item_mv_detail));
    }

    class MvDetailHolder extends BaseViewHolder {
        public MvDetailHolder(View itemView) {
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
