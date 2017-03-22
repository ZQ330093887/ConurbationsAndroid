package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.admin.conurbations.activitys.ShowImageActivity;
import com.test.admin.conurbations.utils.imageloader.ImageLoader;

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
        ImageLoader.displayImage(imageView, mListImages.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, ShowImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("position", mListImages.get(position));
                if (bundle != null) {
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
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