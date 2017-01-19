package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.test.admin.conurbations.widget.GlideImageLoader;
import com.test.admin.conurbations.widget.SolidApplication;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.transformer.AccordionTransformer;

/**
 * Created by wenhuaijun on 2016/2/7 0007.
 */
public class DayAndDayPrettyPictureAdapter extends BaseListAdapter<TSZImageBean> implements OnBannerClickListener {

    private static final int VIEW_TYPE_HEAD = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final TSZImageBean item) {

        if (vh instanceof CategoryHeaderViewHolder) {
            CategoryHeaderViewHolder headerViewHolder = (CategoryHeaderViewHolder) vh;
            headerViewHolder.banner.setImages(SolidApplication.images)
                    .setBannerTitles(SolidApplication.titles)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerClickListener(this)
                    .setBannerAnimation(AccordionTransformer.class)
                    .start();
        }

        if (vh instanceof NormalViewHolder){
            NormalViewHolder normalViewHolder = (NormalViewHolder) vh;
            final RatioImageView imageView = normalViewHolder.getView(R.id.recomend_img1);
            final LabelView labelView = normalViewHolder.getView(R.id.label_view1);
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
            normalViewHolder.setText(R.id.recommend_tip, title)
                    .setText(R.id.recommend_title1, item.getTag().substring(15))
                    .setText(R.id.recommend_content1, item.getUtag());
        }
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEAD) {
            return new CategoryHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_banner, parent, false));
        } else if (viewType == VIEW_TYPE_NORMAL) {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_recommend, parent, false));
        }
        return null;
    }

    public class CategoryHeaderViewHolder extends BaseViewHolder {
        Banner banner;
        public CategoryHeaderViewHolder(View view) {
            super(view);
            banner = (Banner) view.findViewById(R.id.banner);
        }
    }

    @Override
    public void OnBannerClick(int position) {}

    public class NormalViewHolder extends BaseViewHolder {



        public NormalViewHolder(View view) {
            super(view);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type){
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
