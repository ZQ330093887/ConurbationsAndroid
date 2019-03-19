package com.test.admin.conurbations.presenter;


import android.app.Activity;

import com.test.admin.conurbations.activitys.ISearchMusicView;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.api.MusicApiServiceImpl;
import com.test.admin.conurbations.model.db.DaoLitepal;
import com.test.admin.conurbations.model.entity.HotSearchBean;
import com.test.admin.conurbations.model.entity.SearchEngine;
import com.test.admin.conurbations.model.entity.SearchHistoryBean;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.widget.SolidApplication;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class SearchMusicPresenter extends BasePresenter<ISearchMusicView> {

    public SearchMusicPresenter(ISearchMusicView iSearchMusicView) {
        attachView(iSearchMusicView);
    }

    public void searchLocal(String key, Activity context) {
        new Thread(() -> {
            List<Music> result = DaoLitepal.searchLocalMusic(key);
            context.runOnUiThread(() -> mvpView.showSearchResult(result));
        }).start();
    }


    public void search(String key, int limit, int page) {
        Observable<List<Music>> listObservable = Observable.mergeDelayError(
                MusicApiServiceImpl.INSTANCE.getSearchMusicInfo(key, limit, page),
                MusicApiServiceImpl.INSTANCE.searchMusic(key, SearchEngine.Filter.QQ, limit, page),
                MusicApiServiceImpl.INSTANCE.searchMusic(key, SearchEngine.Filter.XIAMI, limit, page),
                MusicApiServiceImpl.INSTANCE.searchMusic(key, SearchEngine.Filter.NETEASE, limit, page));


        ApiManager.request(listObservable, new RequestCallBack<List<Music>>() {
            @Override
            public void success(List<Music> result) {
                LogUtil.e("searchSuccess", result.toString());
                mvpView.showSearchResult(result);
            }

            @Override
            public void error(String msg) {
                LogUtil.e("searchFail", msg);
                mvpView.showSearchResult(new ArrayList<>());
            }
        });
    }

    public void getHotSearchInfo() {
        if (SolidApplication.hotSearchList != null) {
            mvpView.showHotSearchInfo(SolidApplication.hotSearchList);
        } else {
            ApiManager.request(MusicApiServiceImpl.INSTANCE.getHotSearchInfo(), new RequestCallBack<List<HotSearchBean>>() {
                @Override
                public void success(List<HotSearchBean> result) {
                    LogUtil.e("searchSuccess", result.toString());
                    SolidApplication.hotSearchList = result;
                    mvpView.showHotSearchInfo(result);
                }

                @Override
                public void error(String msg) {
                    LogUtil.e("searchFail", msg);
                }
            });
        }
    }

    public void getSearchHistory(Activity activity) {
        new Thread(() -> {
            List<SearchHistoryBean> data = DaoLitepal.getAllSearchInfo(null);
            activity.runOnUiThread(() -> mvpView.showSearchHistory(data));
        }).start();
    }

    public void saveQueryInfo(String query) {
        DaoLitepal.addSearchInfo(query);
    }
}
