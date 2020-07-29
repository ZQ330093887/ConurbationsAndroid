package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.SearchHistoryBean;


/**
 * Created by ZQiong on 2018/11/30
 */
public class SearchHistoryAdapter extends BaseListAdapter<SearchHistoryBean> {


    public SearchHistoryAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final SearchHistoryBean item) {
        vh.setText(R.id.item_suggestion_query, item.title).
                setOnClickListener(R.id.deleteView, getListener(item, vh.getView(R.id.deleteView))).
                setOnClickListener(R.id.history_search, getListener(item, vh.getView(R.id.history_search)));

    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SearchHistoryHolder(inflateItemView(parent, R.layout.item_search_suggestion));
    }

    class SearchHistoryHolder extends BaseViewHolder {
        public SearchHistoryHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private View.OnClickListener getListener(SearchHistoryBean item, View view) {
        return v -> mListener.onItemClick(item, view);
    }


    public interface OnItemClickListener {
        public void onItemClick(SearchHistoryBean item, View view);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
