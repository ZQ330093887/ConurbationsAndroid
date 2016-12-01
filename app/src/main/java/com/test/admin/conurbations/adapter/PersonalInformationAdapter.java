package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.views.CircleImageView;

/**
 * Created by zhouqiong on 2016/11/29.
 */

public class PersonalInformationAdapter extends RecyclerView.Adapter<PersonalInformationAdapter.MyViewHolder> {

    private Context mContext;

    public PersonalInformationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.personal_information_item, null);
        //设置条目的布局方式
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (position == 0) {
            holder.mTopLayout.setVisibility(View.VISIBLE);
            holder.mTopComtentLayout.setVisibility(View.GONE);
        } else {
            holder.mTopLayout.setVisibility(View.GONE);
            holder.mTopComtentLayout.setVisibility(View.VISIBLE);
        }
        holder.tv.setText("text " + position);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        RelativeLayout mTopLayout;
        LinearLayout mTopComtentLayout;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.info_text_one);
            mTopLayout = (RelativeLayout) view.findViewById(R.id.top_info_lly);
            mTopComtentLayout = (LinearLayout) view.findViewById(R.id.top_content_lly);
        }
    }
}
