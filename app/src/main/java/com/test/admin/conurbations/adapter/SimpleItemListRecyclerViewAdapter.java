package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.ItemDetailActivity;
import com.test.admin.conurbations.activitys.ItemDetailFragment;
import com.test.admin.conurbations.model.BookBean;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public class SimpleItemListRecyclerViewAdapter extends BaseListAdapter<BookBean> {

    @Override
    protected void bindDataToItemView(BaseViewHolder holder, final BookBean item) {
        holder.setText(R.id.content, "《" + item.getTitle() + "》");
        holder.setOnClickListener(R.id.content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ITEM_URL, item.getUrl());
                intent.putExtra(ItemDetailFragment.ITEM_TITLE, item.getTitle());
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
