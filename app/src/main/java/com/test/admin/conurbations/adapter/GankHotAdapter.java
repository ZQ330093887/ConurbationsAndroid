package com.test.admin.conurbations.adapter;

import android.content.Context;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.WebViewActivity;
import com.test.admin.conurbations.model.response.GankGirlImageItem;
import com.test.admin.conurbations.model.response.GankHeaderItem;
import com.test.admin.conurbations.model.response.GankImageData;
import com.test.admin.conurbations.model.response.GankItem;
import com.test.admin.conurbations.model.response.GankNormalItem;
import com.test.admin.conurbations.widget.MyStaggeredGridLayoutManager;
import com.test.admin.conurbations.widget.SolidApplication;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;

import javax.inject.Inject;

import cn.leo.click.SingleClick;


/**
 * Created by zhouqiong on 2017/1/12.
 */
public class GankHotAdapter extends BaseListAdapter<List<GankItem>> {

    @Inject
    public GankHotAdapter(Fragment mContext) {
        super(mContext);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, List<GankItem> item) {
        RecyclerView mListRecyclerView = vh.getView(R.id.item_gank_day_list);
        mListRecyclerView.setLayoutManager(new MyStaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL));
        mListRecyclerView.setAdapter(new HomeAdapter(item, ((Fragment) mContext).getContext()));
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new GankHotAdapter.SampleViewHolder(inflateItemView(parent, R.layout.item_gank_day));
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
                    return new CategoryHeaderViewHolder((ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_day_head, parent, false));
                case VIEW_TYPE_NORMAL:
                    return new NormalViewHolder((ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_day_content, parent, false));
                case VIEW_TYPE_GIRL_IMAGE:
                    return new GirlImageViewHolder((ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gank_day_imge, parent, false));
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
                normalHolder.mTitleTextView.setText(getGankTitleStr(normalItem.desc, normalItem.author));
                normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @SingleClick
                    @Override
                    public void onClick(View v) {
                        //
                        WebViewActivity.openUrl(context, normalItem.url, normalItem.desc, false, false);
                    }
                });
                showItemAnim(normalHolder.mTitleTextView, position);
                return;
            }
            if (holder instanceof GirlImageViewHolder) {
                final GirlImageViewHolder girlHolder = (GirlImageViewHolder) holder;
                final GankImageData normalItem = (GankImageData) mItems.get(position);


                girlHolder.mzBannerView.setPages(normalItem.data, (MZHolderCreator<BannerViewHolder>) BannerViewHolder::new);
                girlHolder.mzBannerView.setIndicatorVisible(false);
                girlHolder.mzBannerView.start();

                // TODO: 2019/6/11 点击事件无效，正在找原因
                girlHolder.mzBannerView.setBannerPageClickListener((view, position1) -> {
                    //banner 点击事件
                });
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
            if (gankItem instanceof GankImageData) {
                return VIEW_TYPE_GIRL_IMAGE;
            }
            return VIEW_TYPE_NORMAL;
        }

        public class CategoryHeaderViewHolder extends RecyclerView.ViewHolder {
            TextView mTitleTextView;

            public CategoryHeaderViewHolder(ViewGroup parent) {
                super(parent);
                mTitleTextView = parent.findViewById(R.id.tv_item_gank_day_head_title);
            }
        }

        public class NormalViewHolder extends RecyclerView.ViewHolder {
            TextView mTitleTextView;

            public NormalViewHolder(ViewGroup parent) {
                super(parent);
                mTitleTextView = parent.findViewById(R.id.tv_item_gank_day_content_title);
            }
        }

        public class GirlImageViewHolder extends RecyclerView.ViewHolder {

            MZBannerView mzBannerView;

            public GirlImageViewHolder(ViewGroup parent) {
                super(parent);
                mzBannerView = parent.findViewById(R.id.mzb_banner);
            }
        }

        public class BannerViewHolder implements MZViewHolder<GankGirlImageItem> {
            private ImageView mImageView;

            @Override
            public View createView(Context context) {
                // 返回页面布局文件
                View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
                mImageView = view.findViewById(R.id.banner_image);
                return view;
            }

            @Override
            public void onBind(Context context, int position, GankGirlImageItem girlItem) {
                // 数据绑定
                Glide.with(context)
                        .load(girlItem.image)
                        .placeholder(R.color.white)
                        .centerCrop()
                        .into(mImageView);
                mImageView.setOnClickListener(v -> {
                    WebViewActivity.openUrl(context, girlItem.url, girlItem.title, false, false);
                });
            }
        }
    }
}
