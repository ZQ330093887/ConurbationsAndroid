package com.test.admin.conurbations.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.data.response.GanHuoDataBean;
import com.test.admin.conurbations.utils.RatioImageView;

import java.util.List;

/**
 * Created by zhouqiong on 2015/1/12.
 */
public class WelfareRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<GanHuoDataBean> results;
    private OnRecyclerViewItemClickListener itemOnClickListener;
    private Context context;
    private int mLastPosition = -1;

    /**
     * 设置Item点击监听
     *
     * @param imageItemClickListener
     */
    public void setOnImageItemClickListener(OnRecyclerViewItemClickListener imageItemClickListener) {
        this.itemOnClickListener = imageItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public WelfareRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void addDataList(List<GanHuoDataBean> welfareData) {
        this.results = welfareData;
    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_welfare, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            final NormalViewHolder normalHolder = (NormalViewHolder) holder;

            //ImageUtil.loadImage(resultsBeen.get(position).getUrl(), normalHolder.imageView);

            Glide.with(context)
                    .load(results.get(position).getUrl())
                    .centerCrop()
                    .placeholder(R.color.white)
                    .into(normalHolder.imageView);

            if (itemOnClickListener != null) {
                normalHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = normalHolder.getLayoutPosition();

                        itemOnClickListener.onItemClick(normalHolder.itemView, pos);
                    }
                });
            }
            showItemAnim(normalHolder.imageView, position);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showItemAnim(final View view, final int position) {
        final Context context = view.getContext();
        if (position > mLastPosition) {
            view.setAlpha(0);
            view.postDelayed(new Runnable() {
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            view.setAlpha(1);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(animation);
                }
            }, 138 * position);
            mLastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        if (results != null && results.size() > 0) {
            return results.size();
        } else {
            return 0;
        }
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {
        RatioImageView imageView;

        public NormalViewHolder(View itemView) {
            super(itemView);
            imageView = (RatioImageView) itemView.findViewById(R.id.item_welfare_photo);
            imageView.setRatio(0.618f);
        }
    }
}
