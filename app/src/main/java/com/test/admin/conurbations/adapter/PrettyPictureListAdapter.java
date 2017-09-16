package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.labelview.LabelView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.PrettyPicturesActivity;
import com.test.admin.conurbations.fragments.PrettyPicturesFragment;
import com.test.admin.conurbations.model.entity.TSZImageBean;
import com.test.admin.conurbations.widget.GlideImageLoader;
import com.test.admin.conurbations.widget.SolidApplication;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.transformer.AccordionTransformer;

/**
 * Created by zhouqiong on 2017/1/7
 */
public class PrettyPictureListAdapter extends BaseListAdapter<TSZImageBean> implements OnBannerClickListener {

    private static final int VIEW_TYPE_HEAD = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final TSZImageBean item) {

        if (vh instanceof CategoryHeaderViewHolder) {
            CategoryHeaderViewHolder headerViewHolder = (CategoryHeaderViewHolder) vh;
            headerViewHolder.mBanner.setImages(SolidApplication.images)
                    .setBannerTitles(SolidApplication.titles)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerClickListener(this)
                    .setBannerAnimation(AccordionTransformer.class)
                    .start();
        }

        if (vh instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) vh;
            final String title = item.getUtag().substring(0, item.getUtag().indexOf(" ") + 1);
            LabelView labelView = normalViewHolder.getView(R.id.lv_pretty_picture_recommend);
            labelView.setText(item.getCreate_time().substring(0, 10));
            normalViewHolder.setImageUrlUserGlide(R.id.rv_pretty_picture_recommend, item.getUrl(), 0.918f, R.color.white)
                    .setText(R.id.tv_pretty_picture_recommend_tip, title)
                    .setText(R.id.tv_pretty_picture_recommend_title, item.getTag().substring(15))
                    .setText(R.id.tv_pretty_picture_recommend_content, item.getUtag())
                    .setOnClickListener(R.id.rv_pretty_picture_recommend, v -> {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PrettyPicturesActivity.class);
                        intent.putExtra(PrettyPicturesFragment.CLASS_ID, item.getClass_id());
                        intent.putExtra(PrettyPicturesFragment.CLASS_TITLE, title);
                        context.startActivity(intent);
                    });
        }
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEAD) {
            return new CategoryHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pretty_picture_list_banner, parent, false));
        } else if (viewType == VIEW_TYPE_NORMAL) {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pretty_picture_recommend, parent, false));
        }
        return null;
    }

    public class CategoryHeaderViewHolder extends BaseViewHolder {
        Banner mBanner;

        public CategoryHeaderViewHolder(View view) {
            super(view);
            mBanner = (Banner) view.findViewById(R.id.banner_item_pretty_picture_list_banner);
        }
    }

    @Override
    public void OnBannerClick(int position) {
    }

    public class NormalViewHolder extends BaseViewHolder {
        public NormalViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case VIEW_TYPE_HEAD:
                            return gridManager.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    @Override
    protected int getDataViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEAD;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
}
