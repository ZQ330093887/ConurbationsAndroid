package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.response.PopupItemBean;

/**
 * Created by ZQiong on 2018/11/30
 */
public class BottomDialogItemAdapter extends BaseListAdapter<PopupItemBean> {


    public BottomDialogItemAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final PopupItemBean data) {

        vh.setText(R.id.tv_title, data.title)
                .setImageSource(R.id.iv_icon, data.icon)
                .setColorFilter(R.id.iv_icon, Color.parseColor("#0091EA"))
                .setOnClickListener(R.id.container, getListener(data));


    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new BottomDialogHolder(inflateItemView(parent, R.layout.item_dialog));
    }

    class BottomDialogHolder extends BaseViewHolder {
        public BottomDialogHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private View.OnClickListener getListener(PopupItemBean data) {
        return v -> mListener.onItemClick(data);
    }


    public interface OnItemClickListener {
        public void onItemClick(PopupItemBean data);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
