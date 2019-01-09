package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.INewInformationView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.entity.BaiduList;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.entity.PlaylistInfo;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.MusicUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class NewsInfoListPresenter extends BasePresenter<INewInformationView> {

    @Inject
    public NewsInfoListPresenter() {
    }

    public void loadTopList(final int[] ids, final int limit) {
        ApiManager.request(baiduApiService.getNeteaseRank(ids, limit),
                new RequestCallBack<List<PlaylistInfo>>() {
                    @Override
                    public void success(List<PlaylistInfo> result) {
                        List<NewsList> list = new ArrayList<>();
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
                        mvpView.setNewInfoData(list);
                        mvpView.showFinishState();
                    }

                    @Override
                    public void error(String msg) {
                        mvpView.showError(msg);
                    }
                });
    }


    public void loadBaiDuPlaylist() {
        BaiduApiService baiduApiService = ApiManager.getInstance().create(BaiduApiService.class, Constants.BASE_URL_BAIDU_MUSIC);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_CATEGORY);
        params.put("operator", "1");
        params.put("kflag", "2");
        params.put("format", "json");

        ApiManager.request(baiduApiService.getOnlinePlaylist(params), new RequestCallBack<BaiduList>() {
            @Override
            public void success(BaiduList result) {
                List<NewsList> playlists = new ArrayList<>();
                for (BaiduList.ContentItem item : result.content) {
                    NewsList playlist = new NewsList();
                    playlist.name = item.name;
                    playlist.des = item.comment;
                    playlist.type = Constants.PLAYLIST_BD_ID;
                    playlist.pid = String.valueOf(item.type);
                    playlist.coverUrl = item.picS192;
                    ArrayList<Music> musicList = new ArrayList<>();
                    for (BaiduList.Item itemMusic : item.content) {
                        Music music = new Music();
                        music.title = itemMusic.title;
                        music.album = itemMusic.albumTitle;
                        music.artist = itemMusic.author;
                        music.albumId = itemMusic.albumId;
                        music.mid = itemMusic.songId;
                        musicList.add(music);
                    }
                    playlist.musicList = musicList;
                    playlists.add(playlist);
                }
                mvpView.setNewInfoData(playlists);
                mvpView.showFinishState();
            }

            @Override
            public void error(String msg) {
                mvpView.showError(msg);
            }
        });
    }


    public void loadQQList() {
        ApiManager.request(baiduApiService.getQQRank(3, null), new RequestCallBack<List<PlaylistInfo>>() {
            @Override
            public void success(List<PlaylistInfo> result) {
                List<NewsList> list = new ArrayList<>();
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
                mvpView.setNewInfoData(list);
                mvpView.showFinishState();
            }

            @Override
            public void error(String msg) {
                mvpView.showError(msg);
            }
        });
    }
}
