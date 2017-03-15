package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.NewsDetailActivity;
import com.test.admin.conurbations.fragments.NewsDetailFragment;
import com.test.admin.conurbations.model.News;
import com.test.admin.conurbations.utils.RatioImageView;
import com.test.admin.conurbations.widget.SolidApplication;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class NewsInfoListAdapter extends BaseListAdapter<News> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_PHOTO_ITEM = 1;

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final News item) {
        if (holder instanceof SampleItemViewHolder) {
            SampleItemViewHolder sampleItemViewHolder = (SampleItemViewHolder) holder;
            final RatioImageView imageView = sampleItemViewHolder.getView(R.id.news_summary_photo_iv);
            imageView.setRatio(0.918f);
            sampleItemViewHolder.setTypeface(R.id.news_summary_title_tv, SolidApplication.songTi);
            sampleItemViewHolder.setText(R.id.news_summary_title_tv, item.getTitle());
            if (item.getImages() != null && item.getImages().size() > 0) {
                Glide.with(imageView.getContext())
                        .load(item.getImages().get(0))
                        .centerCrop()
                        .placeholder(R.color.white)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
            sampleItemViewHolder.setOnClickListener(R.id.news_info_card, getListener(sampleItemViewHolder, item));
        }
        if (holder instanceof SamplePhotoItemViewHolder) {
            SamplePhotoItemViewHolder photoItemViewHolder = (SamplePhotoItemViewHolder) holder;
            photoItemViewHolder.setTypeface(R.id.news_summary_title_tv, SolidApplication.songTi);
            photoItemViewHolder.setText(R.id.news_summary_title_tv, item.getTitle());
            photoItemViewHolder.setOnClickListener(R.id.news_info_card, getListener(photoItemViewHolder, item));
        }
    }

    @NonNull
    private View.OnClickListener getListener(final BaseViewHolder holder, final News news) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(NewsDetailFragment.KEY_NEWS, news.getId());
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context
                        , new Pair<>(holder.getView(R.id.news_summary_title_tv), "key_"));
                ActivityCompat.startActivity(context, intent, activityOptionsCompat.toBundle());
            }
        };
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new SampleItemViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_news_info));
            case TYPE_PHOTO_ITEM:
                return new SamplePhotoItemViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_news_info_photo));
        }
        return null;
    }

    class SampleItemViewHolder extends BaseViewHolder {
        public SampleItemViewHolder(ViewGroup parent) {
            super(parent);
        }
    }

    class SamplePhotoItemViewHolder extends BaseViewHolder {
        public SamplePhotoItemViewHolder(ViewGroup parent) {
            super(parent);
        }
    }

    @Override
    protected int getDataViewType(int position) {
        if (list.get(position).getImages() != null && list.get(position).getImages().size() > 0) {
            return TYPE_ITEM;
        } else {
            return TYPE_PHOTO_ITEM;
        }
    }
}
