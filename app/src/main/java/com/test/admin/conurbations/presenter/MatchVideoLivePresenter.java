package com.test.admin.conurbations.presenter;


import android.content.Context;

import com.test.admin.conurbations.activitys.IVideoLiveSourceView;
import com.test.admin.conurbations.activitys.IVideoLiveView;
import com.test.admin.conurbations.model.entity.VideoLiveSource;
import com.test.admin.conurbations.model.response.VideoLiveData;
import com.test.admin.conurbations.utils.TmiaaoUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhouqiong on 2017/5/18.
 */
public class MatchVideoLivePresenter extends BasePresenter {

    private IVideoLiveView videoLiveView;
    private IVideoLiveSourceView iVideoLiveSourceView;

    public MatchVideoLivePresenter(IVideoLiveView videoLiveView) {
        this.videoLiveView = videoLiveView;
    }

    public MatchVideoLivePresenter(IVideoLiveSourceView iVideoLiveSourceView) {
        this.iVideoLiveSourceView = iVideoLiveSourceView;
    }

    public void getVideoLiveInfo() {
        Observable.just(TmiaaoUtils.getLiveList()) // 输入类型
                .map(new Func1<VideoLiveData, VideoLiveData>() {
                    @Override
                    public VideoLiveData call(VideoLiveData videoLiveData) {
                        return TmiaaoUtils.getLiveList();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoLiveData>() {
                    @Override
                    public void call(VideoLiveData videoLiveData) {
                        if (videoLiveData != null && videoLiveData.items.size() > 0) {
                            videoLiveView.setVideoLiveData(videoLiveData);
                        }
                    }
                });
    }

    public void getVideoLiveSourceInfo(final String link, final Context mContext) {
        Observable.just(link)
                .map(new Func1<String, List<VideoLiveSource>>() {
                    @Override
                    public List<VideoLiveSource> call(String link) {
                        return TmiaaoUtils.getSourceList(link).items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoLiveSource>>() {
                    @Override
                    public void call(List<VideoLiveSource> list) {
                        iVideoLiveSourceView.setVideoLiveData(list, mContext);
                    }
                });

    }
}
