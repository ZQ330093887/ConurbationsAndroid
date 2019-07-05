package com.test.admin.conurbations.utils;


import android.util.Base64;

import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.VideoModel;
import com.test.admin.conurbations.model.entity.VideoTT;
import com.test.admin.conurbations.model.response.ResultResponse;
import com.test.admin.conurbations.retrofit.AppClient;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class VideoPathDecoderUtils {
    public static final String TAG = VideoPathDecoderUtils.class.getSimpleName();

    public void decodePath(String srcUrl) {

        AppClient.retrofit().create(GankService.class).getVideoHtml(srcUrl).flatMap(new Function<String, Observable<ResultResponse<VideoModel>>>() {

            @Override
            public Observable<ResultResponse<VideoModel>> apply(String response) throws Exception {
                Pattern pattern = Pattern.compile("videoId: '(.+)'");
                Matcher matcher = pattern.matcher(response);
                if (matcher.find()) {
                    String videoId = matcher.group(1);
                    String r = getRandom();
                    CRC32 crc32 = new CRC32();
                    String s = String.format("/video/urls/v/1/toutiao/mp4/%s?r=%s", videoId, r);
                    //进行crc32加密。
                    crc32.update(s.getBytes());
                    String crcString = crc32.getValue() + "";
                    String url = "http://i.snssdk.com" + s + "&s=" + crcString;
                    return AppClient.retrofit().create(GankService.class).getVideoData(url);
                }
                return null;
            }
        }).map(new Function<ResultResponse<VideoModel>, VideoTT>() {
            @Override
            public VideoTT apply(ResultResponse<VideoModel> videoModelResultResponse) throws Exception {
                VideoModel.VideoListBean data = videoModelResultResponse.data.video_list;

                if (data.video_3 != null) {
                    return updateVideo(data.video_3);
                }
                if (data.video_2 != null) {
                    return updateVideo(data.video_2);
                }
                if (data.video_1 != null) {
                    return updateVideo(data.video_1);
                }
                return null;
            }

            private String getRealPath(String base64) {
                return new String(Base64.decode(base64.getBytes(), Base64.DEFAULT));
            }

            private VideoTT updateVideo(VideoTT video) {
                //base64解码
                video.main_url = getRealPath(video.main_url);
                return video;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<VideoTT>() {
                    @Override
                    public void onNext(VideoTT video) {
                        onSuccess(video.main_url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onDecodeError("解析视频失败，请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    public abstract void onSuccess(String url);

    public abstract void onDecodeError(String errorMsg);
}
