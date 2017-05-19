package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.ItemBeautifulArticleActivity;
import com.test.admin.conurbations.fragments.BeautifulArticleItemDetailFragment;
import com.test.admin.conurbations.model.entity.BooksBean;

/**
 * Created by zhouqiong on 2017/1/12.
 */

public class BeautifulArticleAdapter extends BaseListAdapter<BooksBean> {

    @Override
    protected void bindDataToItemView(BaseViewHolder holder, final BooksBean item) {
        holder.setText(R.id.tv_item_beautiful_article_content, "*  " + item.getTitle())
                .setOnClickListener(R.id.tv_item_beautiful_article_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemBeautifulArticleActivity.class);
                        intent.putExtra(BeautifulArticleItemDetailFragment.ITEM_TITLE_ID, item.getUrl());
                        intent.putExtra("item_title", item.getTitle());
                        context.startActivity(intent);
                    }
                });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SampleViewHolder(inflateItemView(parent, R.layout.item_beautiful_article));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
