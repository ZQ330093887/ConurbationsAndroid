package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IDiscoverView;
import com.test.admin.conurbations.model.api.MusicApiServiceImpl;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.BannerResult;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouqiong on 2016/12/12.
 */

public class DiscoverPresenter extends BasePresenter<IDiscoverView> {


    public DiscoverPresenter(IDiscoverView iDiscoverView) {
        attachView(iDiscoverView);
    }

    public void loadBannerView() {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getBanners(), new RequestCallBack<BannerResult>() {
            @Override
            public void success(BannerResult result) {
                if (result.getCode() == 200) {
                    mvpView.showBannerView(result.getBanners());
                }
            }

            @Override
            public void error(String msg) {
                mvpView.showEmptyView(msg);
            }
        });
    }


    public void loadNetease(String tag) {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getTopPlaylists(tag, 30), new RequestCallBack<List<NewsList>>() {
            @Override
            public void success(List<NewsList> result) {
                mvpView.showNeteaseCharts(result);
            }

            @Override
            public void error(String msg) {
                mvpView.showEmptyView(msg);
            }
        });
    }

    public void loadRaios() {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getRadioChannel(),
                new RequestCallBack<List<NewsList>>() {
                    @Override
                    public void success(List<NewsList> result) {

                        mvpView.showRadioChannels((ArrayList<NewsList>) result);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }

    public void loadArtists() {
        ApiManager.request(MusicApiServiceImpl.INSTANCE.getTopArtists(30, 0),
                new RequestCallBack<List<Artist>>() {
                    @Override
                    public void success(List<Artist> result) {
                        mvpView.showArtistCharts((ArrayList<Artist>) result);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }


}
