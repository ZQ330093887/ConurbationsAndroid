package com.test.admin.conurbations.adapter;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.data.entity.Moment;

/**
 * Created by zhouqiong on 2015/1/12.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {


    private Context context;
    private Moment.Range range;
    private int lastPosition;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if (range == Moment.Range.TWO) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.all_resource_item, parent, false));
        }else {
           return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.all_resource_item, parent, false));
       }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        switch (range) {
            case TWO:
               /* if (datas.get(position).getType().equals("休息视频")){
                    holder.titleTextView.setText(datas.get(position).getDesc());
                }
                ImageUtil.loadImage(datas.get(position).getUrl(),holder.photoView);
                holder.contentAndroidView.setText(datas.get(position).getType());
                holder.contentDataView.setText(datas.get(position).getPublishedAt());
                break;
            case THREE:
                if (toadyDataList != null && toadyDataList.size() > 0) {
                    holder.contentDataView.setText(toadyDataList.get(position).getResults().getWelfare().get(0).getPublishedAt().substring(0, 10));

                    if (toadyDataList.get(position).getResults().getHead().size() > 0) {
                        holder.contentJSView.setText(toadyDataList.get(position).getResults().getHead().get(0).getType());
                    }
                    ImageUtil.loadImage(toadyDataList.get(position).getResults().getWelfare().get(0).getUrl(), holder.photoView);
                    holder.titleTextView.setText(toadyDataList.get(position).getResults().getVideo().get(0).getDesc());
                    holder.contentAndroidView.setText(toadyDataList.get(position).getResults().getAndroid().get(0).getType());
                    holder.contentIOSView.setText(toadyDataList.get(position).getResults().getIOS().get(0).getType());
                }*/
                break;
            case FOUR:
                //holder.mTextView.setText("4");
                break;
            case FIVE:
                //holder.mTextView.setText("5");
                break;
        }
        showItemAnimation(holder, position);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showItemAnimation(MyViewHolder holder, int position) {
        if (position > lastPosition) {
            lastPosition = position;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView, "translationY", 1f * holder.itemView.getHeight(), 0f);
            objectAnimator.setDuration(500);
            objectAnimator.start();
        }
    }


    @Override
    public int getItemCount() {
        //return toadyDataList.size();
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photoView;
        TextView titleTextView;
        TextView contentAndroidView;
        TextView contentIOSView;
        TextView contentJSView;
        TextView contentDataView;

        public MyViewHolder(View itemView) {
            super(itemView);
            photoView = (ImageView) itemView.findViewById(R.id.item_photo);
            titleTextView = (TextView) itemView.findViewById(R.id.item_title);
            contentAndroidView = (TextView) itemView.findViewById(R.id.item_content_android);
            contentIOSView = (TextView) itemView.findViewById(R.id.item_content_ios);
            contentJSView = (TextView) itemView.findViewById(R.id.item_content_js);
            contentDataView = (TextView) itemView.findViewById(R.id.item_content_data);
        }
    }
}
