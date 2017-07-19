package com.test.admin.conurbations.presenter;


import android.content.Context;

import com.test.admin.conurbations.activitys.IVideoLiveSourceView;
import com.test.admin.conurbations.activitys.IVideoLiveView;
import com.test.admin.conurbations.utils.TmiaaoUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhouqiong on 2017/5/18.
 */
public class MatchVideoLivePresenter extends BasePresenter<IVideoLiveView> {

    private IVideoLiveView videoLiveView;
    private IVideoLiveSourceView iVideoLiveSourceView;

    public MatchVideoLivePresenter(IVideoLiveView videoLiveView) {
        this.videoLiveView = videoLiveView;
        attachView(this.videoLiveView);
    }

    public MatchVideoLivePresenter(IVideoLiveSourceView iVideoLiveSourceView) {
        this.iVideoLiveSourceView = iVideoLiveSourceView;
    }

    public void getVideoLiveInfo() {
        Observable.just(TmiaaoUtils.getLiveList())// 输入类型
                .map(videoLiveData -> TmiaaoUtils.getLiveList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoLiveData -> {
                    if (videoLiveData != null && videoLiveData.items.size() > 0) {
                        videoLiveView.setVideoLiveData(videoLiveData);
                    }
                });
    }

    public void getVideoLiveSourceInfo(final String link, final Context mContext) {
        Observable.just(link).map(s -> TmiaaoUtils.getSourceList(link).items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoLiveSources -> iVideoLiveSourceView.setVideoLiveData(videoLiveSources, mContext));
    }
}
