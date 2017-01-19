package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.ItemDetailFragment;
import com.test.admin.conurbations.activitys.ItemListItemActivity;
import com.test.admin.conurbations.model.BooksBean;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public class SimpleItemRecyclerViewAdapter extends BaseListAdapter<BooksBean> {

    @Override
    protected void bindDataToItemView(BaseViewHolder holder, final BooksBean item) {
        holder.setText(R.id.content,  "*  " + item.getTitle());
        holder.setOnClickListener(R.id.content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ItemListItemActivity.class);
                intent.putExtra(ItemDetailFragment.ITEM_TITLE_ID, item.getUrl());
                intent.putExtra("item_title", item.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SampleViewHolder(inflateItemView(parent, R.layout.item_list_content));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
