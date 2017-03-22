package com.test.admin.conurbations.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.BooksBean;

/**
 * Created by zhouqiong on 2017/3/16
 */
public class WonderfulFictionRankAdapter extends  BaseListAdapter<BooksBean>  {

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, BooksBean item) {
        vh.setText(R.id.tv_item_wonderful_fiction_rank_type,item.getType())
                .setText(R.id.tv_item_wonderful_fiction_rank_name,item.getTitle())
                .setText(R.id.tv_item_wonderful_fiction_rank_chapter,item.getTextContent())
                .setText(R.id.tv_item_wonderful_fiction_rank_writer,item.getAuthor())
                .setText(R.id.tv_item_wonderful_fiction_rank_words,item.getScript())
                .setText(R.id.tv_item_wonderful_fiction_rank_time,item.getScriptNumber());
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new WonderfulFictionRankHolder(inflateItemView(parent, R.layout.item_wonderful_fiction_rank));
    }

    class WonderfulFictionRankHolder extends BaseViewHolder {
        public WonderfulFictionRankHolder(View itemView) {
            super(itemView);
        }
    }
}
