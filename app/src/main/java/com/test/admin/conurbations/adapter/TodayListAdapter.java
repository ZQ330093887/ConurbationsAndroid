package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.ShowImageActivity;
import com.test.admin.conurbations.activitys.WebviewActivity;
import com.test.admin.conurbations.data.response.GankGirlImageItem;
import com.test.admin.conurbations.data.response.GankHeaderItem;
import com.test.admin.conurbations.data.response.GankItem;
import com.test.admin.conurbations.data.response.GankNormalItem;
import com.test.admin.conurbations.utils.RatioImageView;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2017/1/12.
 */
public class TodayListAdapter extends BaseListAdapter<List<GankItem>> {
    private Context context;

    public TodayListAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, List<GankItem> item) {
        RecyclerView vhView = vh.getView(R.id.item_today_item_list);
        vhView.setLayoutManager(new MyStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        vhView.setAdapter(new HomeAdapter(item, context));
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new TodayListAdapter.SampleViewHolder(inflateItemView(parent, R.layout.item_list_today));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(View itemView) {
            super(itemView);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<GankItem> mItems;
        Context context;

        private static final int VIEW_TYPE_NORMAL = 1;
        private static final int VIEW_TYPE_HEADER = 2;
        private static final int VIEW_TYPE_GIRL_IMAGE = 3;

        public HomeAdapter(List<GankItem> mItems, Context context) {
            this.mItems = mItems;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    return new CategoryHeaderViewHolder(parent);
                case VIEW_TYPE_NORMAL:
                    return new NormalViewHolder(parent);
                case VIEW_TYPE_GIRL_IMAGE:
                    return new GirlImageViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CategoryHeaderViewHolder) {
                CategoryHeaderViewHolder headerHolder = (CategoryHeaderViewHolder) holder;
                headerHolder.title.setText(((GankHeaderItem) mItems.get(position)).name);
                showItemAnim(headerHolder.title, position);
                return;
            }
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder normalHolder = (NormalViewHolder) holder;
                final GankNormalItem normalItem = (GankNormalItem) mItems.get(position);
                normalHolder.title.setText(getGankTitleStr(normalItem.desc, normalItem.who));
                normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebviewActivity.openUrl((Activity) context, normalItem.url, normalItem.desc);
                    }
                });

                showItemAnim(normalHolder.title, position);
                return;
            }
            if (holder instanceof GirlImageViewHolder) {
                final GirlImageViewHolder girlHolder = (GirlImageViewHolder) holder;
                final GankGirlImageItem girlItem = (GankGirlImageItem) mItems.get(position);
                Glide.with(context)
                        .load(girlItem.imgUrl)
                        .placeholder(R.color.white)
                        .centerCrop()
                        .into(girlHolder.girl_image);
                girlHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPictureActivity(v, girlItem);
                    }
                });
            }
        }

        private void startPictureActivity(View transitView, GankGirlImageItem item) {
            Intent intent = ShowImageActivity.newIntent(transitView.getContext(), item.imgUrl);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) transitView.getContext(), transitView, ShowImageActivity.TRANSIT_PIC);
            try {
                ActivityCompat.startActivity((Activity) transitView.getContext(), intent, optionsCompat.toBundle());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                transitView.getContext().startActivity(intent);
            }
        }

        private CharSequence getGankTitleStr(String desc, String who) {
            if (TextUtils.isEmpty(who)) {
                return desc;
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(desc);
            SpannableString spannableString = new SpannableString(" (" + who + ")");
            spannableString.setSpan(new TextAppearanceSpan(context, R.style.SummaryTextAppearance), 0, spannableString.length(), 0);
            builder.append(spannableString);
            return builder;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            GankItem gankItem = mItems.get(position);
            if (gankItem instanceof GankHeaderItem) {
                return VIEW_TYPE_HEADER;
            }
            if (gankItem instanceof GankGirlImageItem) {
                return VIEW_TYPE_GIRL_IMAGE;
            }
            return VIEW_TYPE_NORMAL;
        }

        public class CategoryHeaderViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.category_title)
            TextView title;

            public CategoryHeaderViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_today_item, parent, false));
                ButterKnife.bind(this, itemView);
            }
        }

        public class NormalViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.title)
            TextView title;

            public NormalViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_gank, parent, false));
                ButterKnife.bind(this, itemView);
            }
        }

        public class GirlImageViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.girl_image)
            RatioImageView girl_image;

            public GirlImageViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_girl_imge, parent, false));
                ButterKnife.bind(this, itemView);
                girl_image.setRatio(1.618f);
            }
        }
    }
}
