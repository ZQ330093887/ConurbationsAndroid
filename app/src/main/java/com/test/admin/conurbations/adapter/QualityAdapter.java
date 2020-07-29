package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.QualitySelectDialog;
import com.test.admin.conurbations.model.Music;

import cn.leo.click.SingleClick;


/**
 * Created by ZQiong on 2018/11/30
 */
public class QualityAdapter extends BaseListAdapter<QualitySelectDialog.QualityItem> {

    private Music music;

    public QualityAdapter(Activity context, Music music) {
        super(context);
        this.music = music;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final QualitySelectDialog.QualityItem item) {
        vh.setText(R.id.tv_title, item.name).setVisibility(R.id.iv_check, (item.quality == music.quality ? View.VISIBLE : View.GONE));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                if (listener!= null){
                    listener.onItemClick(item);
                }
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new QualityDialogHolder(inflateItemView(parent, R.layout.item_quality_select));
    }

    private class QualityDialogHolder extends BaseViewHolder {
        private QualityDialogHolder(View itemView) {
            super(itemView);
        }
    }
}
