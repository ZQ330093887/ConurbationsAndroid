package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.entity.PlaylistInfo;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.MusicUtils;
import com.test.admin.conurbations.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class NeteasePlayListPresenter extends BasePresenter<INewInformationView> {

    @Inject
    public NeteasePlayListPresenter() {
    }

    public void getNeteaseRank(int[] ids, Integer limit) {
        ApiManager.request(baiduApiService.getNeteaseRank(ids, limit),
                new RequestCallBack<List<PlaylistInfo>>() {
                    @Override
                    public void success(List<PlaylistInfo> result) {
                        List<NewsList> list = new ArrayList<>();
                        if (result != null && result.size() > 0) {
                            for (PlaylistInfo info : result) {
                                NewsList playlist = new NewsList();
                                playlist.coverUrl = info.cover;
                                playlist.des = info.description;
                                playlist.pid = info.id;
                                playlist.name = info.name;
                                playlist.type = Constants.PLAYLIST_WY_ID;
                                playlist.playCount = info.playCount;
                                playlist.musicList = MusicUtils.getMusicList(info.list, Constants.NETEASE);
                                list.add(playlist);
                            }
                        }

                        mvpView.setNewInfoData(list);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }


    public void loadQQList(final int[] ids, final Integer limit) {
        ApiManager.request(baiduApiService.getQQRank(limit, ids), new RequestCallBack<List<PlaylistInfo>>() {
            @Override
            public void success(List<PlaylistInfo> result) {
                List<NewsList> list = new ArrayList<>();
                if (result != null && result.size() > 0) {
                    for (PlaylistInfo it : result) {
                        NewsList playlist = new NewsList();
                        playlist.coverUrl = it.cover;
                        playlist.des = it.description;
                        playlist.pid = it.id;
                        playlist.name = it.name;
                        playlist.type = Constants.PLAYLIST_QQ_ID;
                        playlist.playCount = it.playCount;
                        playlist.musicList = MusicUtils.getMusicList(it.list, Constants.QQ);
                        list.add(playlist);
                    }
                }
                mvpView.setNewInfoData(list);
            }

            @Override
            public void error(String msg) {
                ToastUtils.getInstance().showToast(msg);
                System.out.println("***********" + msg);
            }
        });
    }
}
