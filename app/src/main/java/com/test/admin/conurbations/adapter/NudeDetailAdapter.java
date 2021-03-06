package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.PageModel;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2019/1/15.
 */
public class NudeDetailAdapter extends BaseListAdapter<PageModel.ItemModel> {


    @Inject
    public NudeDetailAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final PageModel.ItemModel detail) {


        String url = detail.imgUrl;

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("Referer", url).build());
        ImageView imageView = vh.getView(R.id.image);

        Glide.with(imageView.getContext()).load(glideUrl).asBitmap().into(imageView);

        vh.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, detail);
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new MvDetailHolder(inflateItemView(parent, R.layout.item_nude_detail));
    }

    class MvDetailHolder extends BaseViewHolder {
        public MvDetailHolder(View itemView) {
            super(itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemListener(OnItemClickListener selectImgListener) {
        this.onItemClickListener = selectImgListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, PageModel.ItemModel o);
    }
}
