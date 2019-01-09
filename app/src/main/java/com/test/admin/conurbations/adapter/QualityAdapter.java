package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.QualitySelectDialog;
import com.test.admin.conurbations.model.Music;


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
        vh.itemView.setOnClickListener(getListener(item));
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new QualityDialogHolder(inflateItemView(parent, R.layout.item_quality_select));
    }

    class QualityDialogHolder extends BaseViewHolder {
        public QualityDialogHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private View.OnClickListener getListener(QualitySelectDialog.QualityItem item) {
        return v -> mListener.onItemClick(item);
    }


    public interface OnItemClickListener {
        public void onItemClick(QualitySelectDialog.QualityItem item);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
