package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.labelview.LabelView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.SearchActivity;
import com.test.admin.conurbations.fragments.SearchFragment;
import com.test.admin.conurbations.model.TSZImageBean;
import com.test.admin.conurbations.utils.RatioImageView;

/**
 * Created by wenhuaijun on 2016/2/7 0007.
 */
public class DayAndDayPrettyPictureAdapter extends BaseListAdapter<TSZImageBean> {
    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final TSZImageBean item) {
        final RatioImageView imageView = vh.getView(R.id.recomend_img1);
        final LabelView labelView = vh.getView(R.id.label_view1);
        imageView.setRatio(0.918f);
        final String title = item.getUtag().substring(0, item.getUtag().indexOf(" ") + 1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra(SearchFragment.CLASS_ID, item.getClass_id());
                intent.putExtra(SearchFragment.CLASS_TITLE, title);
                context.startActivity(intent);
            }
        });
        Glide.with(imageView.getContext())
                .load(item.getUrl())
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        labelView.setText(item.getCreate_time().substring(0, 10));
        vh.setText(R.id.recommend_tip, title)
                .setText(R.id.recommend_title1, item.getTag().substring(15))
                .setText(R.id.recommend_content1, item.getUtag());
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SampleTipHolder(inflateItemView(parent, R.layout.itemview_recommend));
    }

    class SampleTipHolder extends BaseViewHolder {
        public SampleTipHolder(View itemView) {
            super(itemView);
        }
    }
}
