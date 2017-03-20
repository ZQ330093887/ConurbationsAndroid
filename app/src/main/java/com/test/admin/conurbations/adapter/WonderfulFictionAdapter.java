package com.test.admin.conurbations.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.labelview.LabelView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.BooksBean;
import com.test.admin.conurbations.utils.RatioImageView;
import com.test.admin.conurbations.widget.DashlineItemDivider;
import com.test.admin.conurbations.widget.GlideImageLoader;
import com.test.admin.conurbations.widget.MyLinearLayoutManager;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.List;

/**
 * Created by zhouqiong on 2017/3/16
 */
public class WonderfulFictionAdapter extends BaseListAdapter<BooksBean> {
    private List<String> titles;
    private List<String[]> images;
    private List<BooksBean> leftBooksBeen;
    private List<BooksBean> rankContentBooksBeen;
    private List<BooksBean> rankTitleBooksBeen;
    private List<BooksBean> updateBooksBeen;
    private static final int VIEW_TYPE_HEAD = 1;
    private static final int VIEW_TYPE_NORMAL = 2;
    private static final int VIEW_TYPE_POPULAR = 3;
    private static final int VIEW_TYPE_LEFT_BOOK_BEAN = 4;
    private static final int VIEW_TYPE_RANK_TITLE = 5;
    private static final int VIEW_TYPE_RANK_TYPE1 = 6;
    private static final int VIEW_TYPE_UPDATE = 7;
    private static final int VIEW_TYPE_UPDATE_NEW = 8;

    @Override
    public void onBindViewHolder(BaseViewHolder vh, int position) {
        super.onBindViewHolder(vh, position);

        if (vh instanceof CategoryHeaderViewHolder) {
            CategoryHeaderViewHolder headerViewHolder = (CategoryHeaderViewHolder) vh;
            headerViewHolder.banner.setImages(images)
                    .setBannerTitles(titles)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                    .setImageLoader(new GlideImageLoader())
                    .setBannerAnimation(AccordionTransformer.class)
                    .start();
        }

        if (vh instanceof NormalViewHolder) {
            BooksBean item = list.get(position - 5);
            NormalViewHolder normalViewHolder = (NormalViewHolder) vh;
            RatioImageView imageView = normalViewHolder.getView(R.id.recomend_img);
            LabelView labelView = normalViewHolder.getView(R.id.label_view);
            LabelView scriptView = normalViewHolder.getView(R.id.wonderful_fiction_scriptNumber_labv);
            imageView.setRatio(0.918f);

            Glide.with(imageView.getContext())
                    .load("http:" + item.getImgUrl())
                    .centerCrop()
                    .placeholder(R.color.white)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            labelView.setText(item.getAuthor());
            scriptView.setText(item.getScriptNumber());
            normalViewHolder.setText(R.id.recommend_title, item.getTitle())
                    .setText(R.id.recommend_content, item.getTextContent())
                    .setText(R.id.item_fiction_type, item.getType())
                    .setText(R.id.item_fiction_script, item.getScript());
        }

        if (vh instanceof LeftBookBeanHolder) {
            LeftBookBeanHolder leftBookBeanHolder = (LeftBookBeanHolder) vh;
            DiscreteScrollView cityPicker = leftBookBeanHolder.getView(R.id.forecast_city_picker);
            if (leftBooksBeen != null && leftBooksBeen.size() > 0) {
                ForecastAdapter forecastAdapter = new ForecastAdapter(leftBooksBeen);
                cityPicker.setAdapter(forecastAdapter);
                cityPicker.scrollToPosition(2);
                cityPicker.setItemTransformer(new ScaleTransformer.Builder()
                        .setMinScale(0.8f)
                        .build());
            }
        }
        if (vh instanceof RankUpdateHolder) {
            RankUpdateHolder rankContentHolder = (RankUpdateHolder) vh;
            RecyclerView mRecyclerView = rankContentHolder.getView(R.id.content);
            mRecyclerView.setLayoutManager(new MyLinearLayoutManager(mRecyclerView.getContext(), GridLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(new ItemWonderfulFictionRankAdapter(updateBooksBeen));
            mRecyclerView.addItemDecoration(new DashlineItemDivider());
        }
        if (vh instanceof RankTitleHolder) {
            RankTitleHolder rankTitleHolder = (RankTitleHolder) vh;
            rankTitleHolder.setText(R.id.title1, rankTitleBooksBeen.get(0).getTitle())
                    .setText(R.id.title2, rankTitleBooksBeen.get(1).getTitle())
                    .setText(R.id.title3, rankTitleBooksBeen.get(2).getTitle())
                    .setText(R.id.title4, rankTitleBooksBeen.get(3).getTitle());
        }

        if (vh instanceof RankTypeHolder) {
            RankTypeHolder rankTypeHolder = (RankTypeHolder) vh;
            rankTypeHolder.setText(R.id.title, "热门小说");
        }

        if (vh instanceof PopularFictionHolder) {
            PopularFictionHolder popularFictionHolder = (PopularFictionHolder) vh;
            popularFictionHolder.setText(R.id.title, "热门榜单");
        }

        if (vh instanceof RankUpdateNewHolder) {
            RankUpdateNewHolder rankUpdateNewHolder = (RankUpdateNewHolder) vh;
            rankUpdateNewHolder.setText(R.id.title, "最近更新");
        }
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final BooksBean item) {
    }

    public void getBannerData(List<String[]> images, List<String> titles, List<BooksBean> leftBooksBeen,
                              List<BooksBean> rankTitleBooksBeen, List<BooksBean> rankContentBooksBeen, List<BooksBean> updateBooksBeen) {
        this.images = images;
        this.titles = titles;
        this.leftBooksBeen = leftBooksBeen;
        this.rankContentBooksBeen = rankContentBooksBeen;
        this.rankTitleBooksBeen = rankTitleBooksBeen;
        this.updateBooksBeen = updateBooksBeen;
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEAD) {
            return new CategoryHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_banner, parent, false));
        } else if (viewType == VIEW_TYPE_NORMAL) {
            return new NormalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction, parent, false));
        } else if (viewType == VIEW_TYPE_LEFT_BOOK_BEAN) {
            return new LeftBookBeanHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction_scall, parent, false));
        } else if (viewType == VIEW_TYPE_POPULAR) {
            return new PopularFictionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction_remen, parent, false));
        } else if (viewType == VIEW_TYPE_RANK_TITLE) {
            return new RankTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction_title, parent, false));
        } else if (viewType == VIEW_TYPE_RANK_TYPE1) {
            return new RankTypeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction_remen, parent, false));
        } else if (viewType == VIEW_TYPE_UPDATE) {
            return new RankUpdateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction_rank_text, parent, false));
        } else if (viewType == VIEW_TYPE_UPDATE_NEW) {
            return new RankUpdateNewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wonderful_fiction_remen, parent, false));
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

    public class NormalViewHolder extends BaseViewHolder {
        public NormalViewHolder(View view) {
            super(view);
        }
    }

    public class LeftBookBeanHolder extends BaseViewHolder {
        public LeftBookBeanHolder(View view) {
            super(view);
        }
    }

    public class PopularFictionHolder extends BaseViewHolder {
        public PopularFictionHolder(View view) {
            super(view);
        }
    }

    public class RankTitleHolder extends BaseViewHolder {
        public RankTitleHolder(View view) {
            super(view);
        }
    }

    public class RankTypeHolder extends BaseViewHolder {
        public RankTypeHolder(View view) {
            super(view);
        }
    }

    public class RankUpdateHolder extends BaseViewHolder {
        public RankUpdateHolder(View view) {
            super(view);
        }
    }

    public class RankUpdateNewHolder extends BaseViewHolder {
        public RankUpdateNewHolder(View view) {
            super(view);
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return gridManager.getSpanCount();
                    } else if (position == 1) {
                        return gridManager.getSpanCount();
                    } else if (position == 2) {
                        return gridManager.getSpanCount();
                    } else if (position == 3) {
                        return gridManager.getSpanCount();
                    } else if (position == 4) {
                        return gridManager.getSpanCount();
                    } else {
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
        } else if (position == 1) {
            return VIEW_TYPE_POPULAR;
        } else if (position == 2) {
            return VIEW_TYPE_RANK_TITLE;
        } else if (position == 3) {
            return VIEW_TYPE_RANK_TYPE1;
        } else if (position == 4) {
            return VIEW_TYPE_LEFT_BOOK_BEAN;
        } else if (position == 17) {
            return VIEW_TYPE_UPDATE_NEW;
        } else if (position == 18) {
            return VIEW_TYPE_UPDATE;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size() + 7;//12
        } else {
            return 0;
        }
    }
}
