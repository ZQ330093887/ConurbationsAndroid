package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.admin.conurbations.R;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2019/1/15.
 */
public class NudeDetailListAdapter extends BaseListAdapter<String> {


    @Inject
    public NudeDetailListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final String url) {

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("Referer", url).build());
        ImageView imageView = vh.getView(R.id.image);

        Glide.with(imageView.getContext()).load(glideUrl).asBitmap().into(imageView);

        vh.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, url);
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new NudeDetailListHolder(inflateItemView(parent, R.layout.item_nude_detail_list));
    }

    class NudeDetailListHolder extends BaseViewHolder {
        public NudeDetailListHolder(View itemView) {
            super(itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemListener(OnItemClickListener selectImgListener) {
        this.onItemClickListener = selectImgListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String o);
    }
}
