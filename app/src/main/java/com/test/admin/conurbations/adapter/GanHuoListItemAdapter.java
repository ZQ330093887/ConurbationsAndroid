package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.admin.conurbations.utils.bigImageView.ImagePreview;
import com.test.admin.conurbations.utils.bigImageView.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouqiong on 2017/1/6.
 */

public class GanHuoListItemAdapter extends PagerAdapter {
    private List<String> mListImages;
    private Context mContext;

    public GanHuoListItemAdapter(Context mContext, List<String> mListImages) {
        this.mListImages = mListImages;
        this.mContext = mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Glide.with(mContext)
                .load(mListImages.get(position))
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


        ImageInfo imageInfo;
        final List<ImageInfo> imageInfoList = new ArrayList<>();
        for (String imgPath : mListImages) {
            imageInfo = new ImageInfo();
            imageInfo.setOriginUrl(imgPath);// 原图
            imageInfo.setThumbnailUrl(imgPath);// 缩略图，实际使用中，根据需求传入缩略图路径。如果没有缩略图url，可以将两项设置为一样，并隐藏查看原图按钮即可。
            imageInfoList.add(imageInfo);
            imageInfo = null;
        }

        imageView.setOnClickListener(view -> {
            ImagePreview
                    .getInstance()
                    .setContext(mContext)
                    .setIndex(0)
                    .setImageInfoList(imageInfoList)
                    .setShowDownButton(true)
                    .setShowOriginButton(true)
                    .setFolderName("肉肉")
                    .setScaleLevel(1, 3, 8)
                    .setZoomTransitionDuration(500)
                    .start();
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return mListImages != null ? mListImages.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}