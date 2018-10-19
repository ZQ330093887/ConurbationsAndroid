package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.activitys.NewsInfoListDetailActivity;
import com.test.admin.conurbations.fragments.NewsInfoListDetailFragment;
import com.test.admin.conurbations.model.entity.News;
import com.test.admin.conurbations.widget.SolidApplication;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class NewsInfoListAdapter extends BaseListAdapter<News> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_PHOTO_ITEM = 1;

    @Inject
    public NewsInfoListAdapter(Fragment context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final News item) {
        if (holder instanceof SampleItemViewHolder) {
            SampleItemViewHolder sampleItemViewHolder = (SampleItemViewHolder) holder;
            sampleItemViewHolder.setTypeface(R.id.tv_news_summary_title, SolidApplication.songTi)
                    .setText(R.id.tv_news_summary_title, item.getTitle())
                    .setImageUrlUserGlide(R.id.rv_news_summary_photo_iv,
                            ((item.getImages() != null && item.getImages().size() > 0) ? item.getImages().get(0) : ""),
                            0.918f, R.color.white)
                    .setOnClickListener(R.id.cv_item_news_info, getListener(item,
                            sampleItemViewHolder.getView(R.id.rv_news_summary_photo_iv),
                            BaseActivity.TRANSLATE_WEB_VIEW_BG_IMG));
        }
        if (holder instanceof SamplePhotoItemViewHolder) {
            SamplePhotoItemViewHolder photoItemViewHolder = (SamplePhotoItemViewHolder) holder;
            photoItemViewHolder.setTypeface(R.id.tv_item_news_summary_title, SolidApplication.songTi)
                    .setText(R.id.tv_item_news_summary_title, item.getTitle())
                    .setOnClickListener(R.id.cv_item_news_info, getListener(item,
                            photoItemViewHolder.getView(R.id.tv_item_news_summary_title),
                            BaseActivity.TRANSLATE_WEB_VIEW_TITLE));
        }
    }

    @NonNull
    private View.OnClickListener getListener(final News news, final View view, final String s) {
        return v -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, NewsInfoListDetailActivity.class);
            intent.putExtra(NewsInfoListDetailFragment.KEY_NEWS, news.getId());
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                    ((Activity) context, new Pair<>(view, s));
            ActivityCompat.startActivity(context, intent, activityOptionsCompat.toBundle());
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
