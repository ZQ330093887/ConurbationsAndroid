package com.test.admin.conurbations.adapter;

import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.admin.conurbations.utils.RatioImageView;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;

/**
 * Created by zhouqiong on 2017/1/2.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    private final SparseArray<View> views;

    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int id, String string) {
        TextView textView = getView(id);
        textView.setText(string);
        return this;
    }

    public BaseViewHolder setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public BaseViewHolder setTypeface(int id, Typeface tf) {
        TextView textView = getView(id);
        textView.setTypeface(tf);
        return this;
    }

    public BaseViewHolder setBackground(int id, int resId) {
        getView(id).setBackgroundResource(resId);
        return this;
    }

    public BaseViewHolder setOnClickListener(int id, View.OnClickListener onClickListener) {
        getView(id).setOnClickListener(onClickListener);
        return this;
    }

    public BaseViewHolder setImageSource(int id, int resId) {
        ImageView imageView = getView(id);
        imageView.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setColorFilter(int id, int color) {
        ImageView imageView = getView(id);
        imageView.setColorFilter(color);
        return this;
    }

    //这个功能比较比较常用，大概写了一个
    public BaseViewHolder setImageUrlUserGlide(int id, String imgUrl, float ratio, int resId) {
        RatioImageView ratioImageView = getView(id);
        ratioImageView.setRatio(ratio);
        Glide.with(ratioImageView.getContext())
                .load(imgUrl)
                .centerCrop()
                .placeholder(resId)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ratioImageView);
        return this;
    }

    public BaseViewHolder setImageUrlUserGlide(final ImageView view, String imgUrl, int resId) {
        Glide.with(view.getContext())
                .load(imgUrl)
                .centerCrop()
                .placeholder(resId)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
        return this;
    }

    public BaseViewHolder setImageUrl(int id, String url) {
        final ImageView imageView = getView(id);
        ImageUtil.loadImage(url, imageView);
        return this;
    }

    public BaseViewHolder setVisibility(int id, int visibility) {
        getView(id).setVisibility(visibility);
        return this;
    }

    public BaseViewHolder startAnimation(int id, Animation animation) {
        getView(id).startAnimation(animation);
        return this;
    }

    public BaseViewHolder setTag(int id, Object object) {
        getView(id).setTag(object);
        return this;
    }

    public BaseViewHolder setChecked(int id, boolean checked) {
        ((CompoundButton) getView(id)).setChecked(checked);
        return this;
    }

    public BaseViewHolder setTextColor(int id, int color) {
        ((TextView) getView(id)).setTextColor(color);
        return this;
    }

    public BaseViewHolder setOnCheckedChangeListener(int id, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        ((CompoundButton) getView(id)).setOnCheckedChangeListener(onCheckedChangeListener);
        return this;
    }

    public BaseViewHolder setOnTouchListener(int id, View.OnTouchListener onTouchListener) {
        getView(id).setOnTouchListener(onTouchListener);
        return this;
    }

    public Object getTag(int id) {
        return getView(id).getTag();
    }
}
