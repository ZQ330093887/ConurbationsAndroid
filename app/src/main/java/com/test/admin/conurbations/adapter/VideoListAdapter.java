package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.VideoDetailActivity;
import com.test.admin.conurbations.model.entity.LeVideoData;
import com.test.admin.conurbations.model.entity.TTNews;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.DateUtils;
import com.test.admin.conurbations.utils.DisplayUtils;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.VideoPathDecoderUtils;
import com.test.admin.conurbations.utils.rom.UIUtils;

import org.jsoup.helper.DataUtil;

import javax.inject.Inject;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * VideoListAdapter
 * Created by zhouqiong on 2019/4/2.
 */

public class VideoListAdapter extends BaseListAdapter<TTNews> {

    private Context context;

    @Inject
    public VideoListAdapter(Fragment context) {
        super(context);
        this.context = context.getContext();
    }

    @Override
    protected void bindDataToItemView(final BaseViewHolder helper, final TTNews news) {
        if (TextUtils.isEmpty(news.title)) {
            //如果没有标题，则直接跳过
            return;
        }

        JCVideoPlayerStandard videoPlayer = helper.getView(R.id.video_player);

        helper.setVisibility(R.id.tv_item_news_video_time, VISIBLE)//显示时长
                .setText(R.id.tv_item_news_video_time, DateUtils.secToTime(news.video_duration));//设置时长

        String videoUrl = "";
        if (news.video_detail_info != null) {
            //取出解析后的网址
            videoUrl = news.video_detail_info.parse_video_url;
            helper.setImageUrlUserGlide(videoPlayer.thumbImageView, news.video_detail_info.detail_video_large_image.url, R.color.white);
        }
        videoPlayer.setUp("", JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, news.title);
        if (!TextUtils.isEmpty(videoUrl)) {
            //如果已经解析过
            videoPlayer.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, news.title);

        } else {
            //解析视频

            parseVideo(news, videoPlayer);
        }

    }

    boolean isVideoParsing = false; //视频是否在解析的标识

    private void parseVideo(TTNews news, JCVideoPlayerStandard videoPlayer) {

        if (isVideoParsing) {
            return;
        } else {
            isVideoParsing = true;
        }

        VideoPathDecoderUtils decoder = new VideoPathDecoderUtils() {
            @Override
            public void onSuccess(String url) {
                UIUtils.INSTANCE.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        //更改视频是否在解析的标识
                        isVideoParsing = false;

                        //准备播放
                        videoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, news.title);

                        if (news.video_detail_info != null) {
                            news.video_detail_info.parse_video_url = url; //赋值解析后的视频地址
                            videoPlayer.seekToInAdvance = (int) news.video_detail_info.progress; //设置播放进度
                        }

                        //开始播放
                        videoPlayer.startVideo();
                    }
                });
            }

            @Override
            public void onDecodeError(String errorMsg) {
                isVideoParsing = false;//更改视频是否在解析的标识
                //隐藏加载中 显示开始按钮
            }
        };
        if (!TextUtils.isEmpty(news.url)) {
            decoder.decodePath(news.url);
        }
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new VideoListAdapter.SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_video_list));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(ViewGroup parent) {
            super(parent);
        }
    }
}
