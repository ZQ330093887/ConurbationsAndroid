package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.Moment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2015/1/12.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private List<Integer> datas;
    private Context context;
    private List<Integer> lists;
    private Moment.Range range;
    private View view;

    public MyRecyclerViewAdapter(Context context, List<Integer> datas , Moment.Range range) {
        this.datas = datas;
        this.context = context;
        this.range = range;
        getRandomHeights(datas);
    }

    private void getRandomHeights(List<Integer> datas) {
        lists = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            lists.add((int) (200 + Math.random() * 400));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = lists.get(position);//把随机的高度赋予item布局
        switch (range){
            case ONE:
                holder.itemView.setLayoutParams(params);
                holder.mTextView.setText(position+"1");
                break;
            case TWO:
                holder.mTextView.setText("2");
                break;
            case THREE:
                holder.mTextView.setText("3");
                break;
            case FOUR:
                holder.mTextView.setText("4");
                break;
            case FIVE:
                holder.mTextView.setText("5");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
