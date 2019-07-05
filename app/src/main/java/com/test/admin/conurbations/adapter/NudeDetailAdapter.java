package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.FullScreenImageActivity;
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
        String host = "";
        if (url.startsWith("https://")) {
            host = url.replace("https://", "");
        } else if (url.startsWith("http://")) {
            host = url.replace("http://", "");
        }
        host = host.substring(0, host.indexOf("/"));
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E)  Chrome/60.0.3112.90 Mobile Safari/537.36")
                .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                .addHeader("Host", host)
                .addHeader("Proxy-Connection", "keep-alive")
                .addHeader("Referer", "http://m.mzitu.com/");
        GlideUrl glideUrl = new GlideUrl(url, builder.build());

        ImageView imageView = vh.getView(R.id.image);

        Glide.with(imageView.getContext()).load(glideUrl).asBitmap().into(imageView);
        vh.itemView.setTag(url);
        vh.itemView.setOnClickListener(this::onClickListener);
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

    private void onClickListener(View view) {
        String url = (String) view.getTag();
        view.getContext().startActivity(FullScreenImageActivity.Companion.newInstance(view.getContext(), url));
    }
}
