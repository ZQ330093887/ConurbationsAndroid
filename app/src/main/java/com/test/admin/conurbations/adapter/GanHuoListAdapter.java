package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.WebviewActivity;
import com.test.admin.conurbations.data.response.GanHuoDataBean;
import com.test.admin.conurbations.utils.DateUtils;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class GanHuoListAdapter extends BaseListAdapter<GanHuoDataBean> {

    private static final int VIEW_TYPE_HAVE_IMAGE = 1;
    private static final int VIEW_TYPE_HAVE_TEXT = 2;

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final GanHuoDataBean item) {
        String date = item.getPublishedAt().replace('T', ' ').replace('Z', ' ');
        holder.setText(R.id.tv_title, item.getDesc())
                .setText(R.id.tv_people, "via " + item.getWho())
                .setText(R.id.tv_time, DateUtils.friendlyTime(date));
        holder.itemView.setOnClickListener(setOnClickListener(item));
        if (item.getImages() != null && item.getImages().size() > 0) {
            ViewPager viewPager = holder.getView(R.id.viewpager);
            viewPager.setAdapter(new GanHuoListItemAdapter(viewPager.getContext(), item.getImages()));
            holder.setOnClickListener(R.id.rl_bottom, setOnClickListener(item));
        }
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HAVE_IMAGE:
                return new SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_ganhuo_image));
            case VIEW_TYPE_HAVE_TEXT:
                return new SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_ganhuo_text));
        }
        return null;
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(ViewGroup parent) {
            super(parent);
        }
    }

    private View.OnClickListener setOnClickListener(final GanHuoDataBean item) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebviewActivity.openUrl((Activity) v.getContext(), item.getUrl(), item.getDesc());
            }
        };
        return onClickListener;
    }

    @Override
    protected int getDataViewType(int position) {
        if (list != null && list.size() > 0) {
            if (list.get(position).getImages() != null) {
                return VIEW_TYPE_HAVE_IMAGE;
            }
        } else {
            return VIEW_TYPE_HAVE_TEXT;
        }
        return VIEW_TYPE_HAVE_TEXT;
    }
}
