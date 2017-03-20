package com.test.admin.conurbations.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.BooksBean;

import java.util.List;

/**
 * Created by zhouqiong on 2017/3/16
 */
public class ItemWonderfulFictionRankAdapter extends RecyclerView.Adapter<ItemWonderfulFictionRankAdapter.ViewHolder> {

    private List<BooksBean> data;

    public ItemWonderfulFictionRankAdapter(List<BooksBean> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_adapter_wonderful_fiction_rank_content, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BooksBean forecast = data.get(position);
        holder.type.setText(forecast.getType());
        holder.name.setText(forecast.getTitle());
        holder.chapter.setText(forecast.getTextContent());
        holder.writer.setText(forecast.getAuthor());
        holder.words.setText(forecast.getScript());
        holder.time.setText(forecast.getScriptNumber());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView type;
        private TextView name;
        private TextView chapter;
        private TextView writer;
        private TextView words;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.item_content_rank_type);
            name = (TextView) itemView.findViewById(R.id.item_content_rank_name);
            chapter = (TextView) itemView.findViewById(R.id.item_content_rank_chapter);
            writer = (TextView) itemView.findViewById(R.id.item_content_rank_writer);
            words = (TextView) itemView.findViewById(R.id.item_content_rank_words);
            time = (TextView) itemView.findViewById(R.id.item_content_rank_time);
        }
    }
}
