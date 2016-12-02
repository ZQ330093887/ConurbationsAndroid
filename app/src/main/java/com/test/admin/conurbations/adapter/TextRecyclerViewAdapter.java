package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.models.VO.Moment;

import java.util.List;

/**
 * Created by zhouqiong on 2015/1/12.
 */
public class TextRecyclerViewAdapter extends RecyclerView.Adapter<TextRecyclerViewAdapter.TextViewHolder> {
    private List<Integer> datas;
    private Context context;
    private Moment.Range range;
    private View view;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }

    public TextRecyclerViewAdapter(Context context, List<Integer> datas, Moment.Range range) {
        this.datas = datas;
        this.context = context;
        this.range = range;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_text, parent, false);
        TextViewHolder viewHolder = new TextViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, final int position) {
        switch (range){
            case ONE:
                holder.item_tv_text.setText(datas.get(position)+"精选");
                break;
            case TWO:
                holder.item_tv_text.setText(datas.get(position)+"竞技");
                break;
            case THREE:
                holder.item_tv_text.setText(datas.get(position)+"热榜");
                break;
            case FOUR:
                holder.item_tv_text.setText(datas.get(position)+"录像");
                break;
        }

        if (mOnItemClickListener != null){
            holder.item_tv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

            holder.item_tv_text.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv_text;
        public TextViewHolder(View itemView) {
            super(itemView);
            item_tv_text = (TextView) itemView.findViewById(R.id.item_tv_text);
        }
    }
}

