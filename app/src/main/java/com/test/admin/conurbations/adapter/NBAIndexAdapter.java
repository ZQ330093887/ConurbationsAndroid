package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.activitys.NewsInfoListDetailActivity;
import com.test.admin.conurbations.fragments.NewsInfoListDetailFragment;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsItemBean;
import com.test.admin.conurbations.model.entity.VideoInfo;
import com.test.admin.conurbations.utils.RatioImageView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by zhouqiong on 2016/12/30.
 */

public class NBAIndexAdapter extends BaseListAdapter<NewsItemBean> {

    private static final int VIEW_TYPE_VIDEO = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final NewsItemBean item) {
        if (vh instanceof SampleVideoViewHolder) {
            SampleVideoViewHolder videoViewHolder = (SampleVideoViewHolder) vh;
            final JCVideoPlayerStandard videoPlayerStandard = videoViewHolder.getView(R.id.jps_item_news_video_video);

            videoPlayerStandard.setUp("", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, item.title);
            if (TextUtils.isEmpty(item.realUrl)) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://h5vv.video.qq.com")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .client(new OkHttpClient.Builder().build()).build();
                GankService api = retrofit.create(GankService.class);
                Call<String> call = api.getVideoRealUrls(item.vid);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response != null && !TextUtils.isEmpty(response.body())) {
                            String resp = response.body()
                                    .replaceAll("QZOutputJson=", "")
                                    .replaceAll(" ", "")
                                    .replaceAll("\n", "");
                            if (resp.endsWith(";")) {
                                resp = resp.substring(0, resp.length() - 1);
                            }

                            VideoInfo real = new Gson().fromJson(resp, VideoInfo.class);
                            if (real.vl != null) {
                                String vid = real.vl.vi.get(0).vid;
                                String vkey = real.vl.vi.get(0).fvkey;
                                String url = real.vl.vi.get(0).ul.ui.get(0).url + vid + ".mp4?vkey=" + vkey;
                                item.realUrl = url;
                                videoPlayerStandard.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, item.title);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
            } else {
                videoPlayerStandard.setUp(item.realUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, item.title);
            }
            Glide.with(videoPlayerStandard.thumbImageView.getContext())
                    .load(item.imgurl)
                    .centerCrop()
                    .placeholder(R.color.white)
                    .crossFade()
                    .into(videoPlayerStandard.thumbImageView);
            videoViewHolder.setText(R.id.tv_item_news_video_time, item.pub_time);
        } else if (vh instanceof SampleNormalViewHolder) {
            SampleNormalViewHolder normalViewHolder = (SampleNormalViewHolder) vh;
            final RatioImageView mPhotoRatioImageView = normalViewHolder.getView(R.id.rv_item_news_normal_banner_image);
            mPhotoRatioImageView.setRatio(1.9f);
            Glide.with(mPhotoRatioImageView.getContext())
                    .load(item.imgurl)
                    .centerCrop()
                    .placeholder(R.color.white)
                    .crossFade()
                    .into(mPhotoRatioImageView);
            normalViewHolder.setText(R.id.tv_item_news_normal_banner_title, item.title)
                    .setText(R.id.tv_item_news_normal_banner_time, item.pub_time);
            normalViewHolder.setOnClickListener(R.id.cv_item_news_normal_banner, getListener(item,
                    vh.getView(R.id.rv_item_news_normal_banner_image),
                    BaseActivity.TRANSLATE_WEB_VIEW_BG_IMG));
        }
    }

    @NonNull
    private View.OnClickListener getListener(final NewsItemBean news, final View view, final String s) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NewsInfoListDetailActivity.class);
                intent.putExtra(NewsInfoListDetailFragment.KEY_NBA_INDEX, news.index);
                intent.putExtra(NewsInfoListDetailFragment.KEY_TITLE, news.title);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                        ((Activity) context, new Pair<>(view, s));
                ActivityCompat.startActivity(context, intent, activityOptionsCompat.toBundle());
            }
        };
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_VIDEO) {
            return new SampleVideoViewHolder(inflateItemView(parent, R.layout.item_list_news_video));
        } else if (viewType == VIEW_TYPE_NORMAL) {
            return new SampleNormalViewHolder(inflateItemView(parent, R.layout.item_list_news_normal));
        }
        return null;
    }

    class SampleNormalViewHolder extends BaseViewHolder {
        public SampleNormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    class SampleVideoViewHolder extends BaseViewHolder {
        public SampleVideoViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    protected int getDataViewType(int position) {
        NewsItemBean itemBean = list.get(position);
        if (itemBean.atype.equals("2")) {
            return VIEW_TYPE_VIDEO;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
}
