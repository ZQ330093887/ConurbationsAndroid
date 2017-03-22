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
import com.test.admin.conurbations.activitys.WebViewActivity;
import com.test.admin.conurbations.model.response.GankGirlImageItem;
import com.test.admin.conurbations.model.response.GankHeaderItem;
import com.test.admin.conurbations.model.response.GankItem;
import com.test.admin.conurbations.model.response.GankNormalItem;
import com.test.admin.conurbations.utils.RatioImageView;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2017/1/12.
 */
public class GankDayAdapter extends BaseListAdapter<List<GankItem>> {
    private Context mContext;

    public GankDayAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, List<GankItem> item) {
        RecyclerView mListRecyclerView = vh.getView(R.id.item_gank_day_list);
        mListRecyclerView.setLayoutManager(new MyStaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        mListRecyclerView.setAdapter(new HomeAdapter(item, mContext));
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new GankDayAdapter.SampleViewHolder(inflateItemView(parent, R.layout.item_gank_day));
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
                headerHolder.mTitleTextView.setTypeface(SolidApplication.songTi);
                headerHolder.mTitleTextView.setText(((GankHeaderItem) mItems.get(position)).name);
                showItemAnim(headerHolder.mTitleTextView, position);
                return;
            }
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder normalHolder = (NormalViewHolder) holder;
                final GankNormalItem normalItem = (GankNormalItem) mItems.get(position);
                normalHolder.mTitleTextView.setText(getGankTitleStr(normalItem.desc, normalItem.who));
                normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.openUrl((Activity) context, normalItem.url, normalItem.desc);
                    }
                });

                showItemAnim(normalHolder.mTitleTextView, position);
                return;
            }
            if (holder instanceof GirlImageViewHolder) {
                final GirlImageViewHolder girlHolder = (GirlImageViewHolder) holder;
                final GankGirlImageItem girlItem = (GankGirlImageItem) mItems.get(position);
                Glide.with(context)
                        .load(girlItem.imgUrl)
                        .placeholder(R.color.white)
                        .centerCrop()
                        .into(girlHolder.mImageView);
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
                ActivityCompat.startActivity(transitView.getContext(), intent, optionsCompat.toBundle());
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
            spannableString.setSpan(new TextAppearanceSpan(context, R.style.SummaryTextAppearance),
                    0, spannableString.length(), 0);
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

            @Bind(R.id.tv_item_gank_day_head_title)
            TextView mTitleTextView;

            public CategoryHeaderViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_day_head, parent, false));
                ButterKnife.bind(this, itemView);
            }
        }

        public class NormalViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_item_gank_day_content_title)
            TextView mTitleTextView;

            public NormalViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_day_content, parent, false));
                ButterKnife.bind(this, itemView);
            }
        }

        public class GirlImageViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.rv_item_gank_day_image)
            RatioImageView mImageView;

            public GirlImageViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_day_imge, parent, false));
                ButterKnife.bind(this, itemView);
                mImageView.setRatio(1.618f);
            }
        }
    }
}
