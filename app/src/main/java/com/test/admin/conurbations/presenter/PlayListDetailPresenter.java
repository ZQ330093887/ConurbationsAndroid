package com.test.admin.conurbations.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.admin.conurbations.activitys.IPlayListDetailView;
import com.test.admin.conurbations.activitys.IWelfareView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.MusicInfo;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.api.MusicApiServiceImpl;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.user.UserStatus;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.MusicUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.download.PlaylistLoader;
import com.test.admin.conurbations.utils.download.SongLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class PlayListDetailPresenter extends BasePresenter<IPlayListDetailView> {

    public PlayListDetailPresenter(IPlayListDetailView iPlayListDetailView) {
        attachView(iPlayListDetailView);
    }

    public void loadPlaylistSongs(NewsList playlist) {
        switch (playlist.type) {
            case Constants.PLAYLIST_LOCAL_ID:
            case Constants.PLAYLIST_HISTORY_ID:
            case Constants.PLAYLIST_LOVE_ID:
            case Constants.PLAYLIST_QUEUE_ID:
                if (!TextUtils.isEmpty(playlist.pid)) {
                    List<Music> data = PlaylistLoader.getMusicForPlaylist(playlist.pid, playlist.order);
                    mvpView.showPlaylistSongs(data);
                }
                break;
            case Constants.PLAYLIST_BD_ID:
                ApiManager.request(MusicApiServiceImpl.INSTANCE.getRadioChannelInfo(playlist),
                        new RequestCallBack<NewsList>() {
                            @Override
                            public void success(NewsList result) {
                                mvpView.showPlaylistSongs(result.musicList);
                            }

                            @Override
                            public void error(String msg) {
                                ToastUtils.getInstance().showToast(msg);
                                System.out.println("***********" + msg);
                            }
                        });
                break;
            case Constants.PLAYLIST_WY_ID:
                ApiManager.request(MusicApiServiceImpl.INSTANCE.getPlaylistDetail(playlist.pid),
                        new RequestCallBack<NewsList>() {
                            @Override
                            public void success(NewsList result) {
                                mvpView.showPlaylistSongs(result.musicList);
                            }

                            @Override
                            public void error(String msg) {
                                ToastUtils.getInstance().showToast(msg);
                                System.out.println("***********" + msg);
                            }
                        });
                break;
            default:
                BaiduApiService baiduApiService = ApiManager.getInstance().create(BaiduApiService.class, Constants.BASE_PLAYER_URL);
                ApiManager.request(baiduApiService.getMusicList("", playlist.pid),
                        new RequestCallBack<ResponseBody>() {
                            @Override
                            public void success(ResponseBody result) {
                                try {
                                    String json = result.string();
                                    List<MusicInfo> data = new Gson().fromJson(json, new TypeToken<List<MusicInfo>>() {
                                    }.getType());

                                    List<Music> musicList = new ArrayList<>();
                                    for (MusicInfo musicInfo : data) {
                                        Music music = MusicUtils.getMusic(musicInfo);
                                        musicList.add(music);
                                    }

                                    if (data.isEmpty() && json.contains("msg")) {
                                        ToastUtils.getInstance().showToast("报错了！！！");
                                    } else {
                                        Iterator<Music> iterator = musicList.iterator();
                                        while (iterator.hasNext()) {
                                            Music temp = iterator.next();
                                            if (temp.isCp) {
                                                iterator.remove();// 推荐使用
                                            }
                                        }
                                        mvpView.showPlaylistSongs(musicList);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void error(String msg) {
                                ToastUtils.getInstance().showToast(msg);
                                System.out.println("***********" + msg);
                            }
                        });
                break;
        }
    }


    public void loadArtistSongs(Artist artist) {

        if (artist.type == null || artist.type.equals(Constants.LOCAL)) {
            List<Music> data = SongLoader.getSongsForArtist(artist.name);
            mvpView.showPlaylistSongs(data);
        } else if (artist.type.equals(Constants.BAIDU)) {
            HashMap<String, Object> params = new HashMap<>();
            params.put(Constants.PARAM_TING_UID, artist.artistId);
            params.put(Constants.PARAM_OFFSET, 0);
            params.put(Constants.PARAM_LIMIT, 20);

            ApiManager.request(MusicApiServiceImpl.INSTANCE.getArtistSongList(params),
                    new RequestCallBack<Artist>() {
                        @Override
                        public void success(Artist result) {
                            mvpView.showPlaylistSongs(result.songs);
                        }

                        @Override
                        public void error(String msg) {
                            ToastUtils.getInstance().showToast(msg);
                            System.out.println("***********" + msg);
                        }
                    });
            return;
        }

        Observable<Artist> observable = MusicApiServiceImpl.INSTANCE.getArtistSongs(artist.type, artist.artistId, 50, 0);
        ApiManager.request(observable,
                new RequestCallBack<Artist>() {
                    @Override
                    public void success(Artist result) {
                        List<Music> musicLists = result.songs;
                        Iterator<Music> iterator = musicLists.iterator();
                        while (iterator.hasNext()) {
                            Music temp = iterator.next();
                            if (temp.isCp) {
                                //list.remove(temp);// 出现java.util.ConcurrentModificationException
                                iterator.remove();// 推荐使用
                            }
                        }
                        mvpView.showPlaylistSongs(musicLists);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }

    public void loadAlbumSongs(Album album) {

        if (album.type == null || album.type.equals(Constants.LOCAL)) {
            List<Music> data = SongLoader.getSongsForAlbum(album.name);
            mvpView.showPlaylistSongs(data);
            return;
        } else if (album.albumId == null) {
            mvpView.showPlaylistSongs(null);
            return;
        } else if (album.type.equals(Constants.BAIDU)) {
            ApiManager.request(MusicApiServiceImpl.INSTANCE.getAlbumSongList(album.albumId),
                    new RequestCallBack<Album>() {
                        @Override
                        public void success(Album result) {
                            mvpView.showPlaylistSongs(album.songs);
                        }

                        @Override
                        public void error(String msg) {
                            ToastUtils.getInstance().showToast(msg);
                            System.out.println("***********" + msg);
                        }
                    });
            return;
        }

        Observable<List<Music>> observable = MusicApiServiceImpl.INSTANCE.getAlbumSongs(album.type, album.albumId);
        ApiManager.request(observable,
                new RequestCallBack<List<Music>>() {
                    @Override
                    public void success(List<Music> result) {
                        Iterator<Music> iterator = result.iterator();
                        while (iterator.hasNext()) {
                            Music temp = iterator.next();
                            if (temp.isCp) {
                                //list.remove(temp);// 出现java.util.ConcurrentModificationException
                                iterator.remove();// 推荐使用
                            }
                        }
                        mvpView.showPlaylistSongs(result);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }

    public void renamePlaylist(NewsList playlist, String title) {

        Observable<String> observable = MusicApiServiceImpl.INSTANCE.renamePlaylist(playlist.pid, title);
        ApiManager.request(observable,
                new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        mvpView.success(1);
                        playlist.name = title;
                        RxBus.getDefault().post(new Event(playlist, String.valueOf(Constants.PLAYLIST_RENAME)));
                        ToastUtils.getInstance().showToast(result);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }
}
