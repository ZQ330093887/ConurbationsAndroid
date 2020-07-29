package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.HotSearchBean;

import cn.leo.click.SingleClick;


/**
 * Created by ZQiong on 2018/11/30
 */
public class HotSearchAdapter extends BaseListAdapter<HotSearchBean> {


    public HotSearchAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final HotSearchBean item) {
        vh.setText(R.id.titleTv, item.title).
                setOnClickListener(R.id.titleTv, getListener(item));

    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new HotSearchHolder(inflateItemView(parent, R.layout.item_search_hot));
    }

    class HotSearchHolder extends BaseViewHolder {
        public HotSearchHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private View.OnClickListener getListener(HotSearchBean item) {
        return new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            }
        };
    }
}
