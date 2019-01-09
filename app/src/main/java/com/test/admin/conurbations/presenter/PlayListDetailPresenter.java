package com.test.admin.conurbations.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.admin.conurbations.activitys.IPlayListDetailView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.MusicInfo;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.api.MusicApiServiceImpl;
import com.test.admin.conurbations.model.entity.Album;
import com.test.admin.conurbations.model.entity.AlbumSongList;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.model.entity.ArtistMusicList;
import com.test.admin.conurbations.model.entity.BaiduMusicList;
import com.test.admin.conurbations.model.entity.NeteasePlaylistDetail;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.entity.RadioChannelData;
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

public class PlayListDetailPresenter {
    private BaiduApiService baiduApiService = ApiManager.getInstance().create(BaiduApiService.class, Constants.BASE_URL_BAIDU_MUSIC);

    private String token;
    private IPlayListDetailView mvpView;

    public PlayListDetailPresenter(IPlayListDetailView iPlayListDetailView) {
        this.mvpView = iPlayListDetailView;
        if (UserStatus.getUserInfo() != null) {
            token = UserStatus.getUserInfo().token;
        }

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
                ApiManager.request(baiduApiService.getRadioChannelSongs(playlist.pid),
                        new RequestCallBack<RadioChannelData>() {
                            @Override
                            public void success(RadioChannelData result) {
                                List<Music> songs = new ArrayList<>();
                                if (result.errorCode == 22000) {
                                    if (result.result.songlist != null) {
                                        for (RadioChannelData.CHSongInfo it : result.result.songlist) {

                                            if (it.songid != null) {
                                                Music music = new Music();
                                                music.type = Constants.BAIDU;
                                                music.title = it.title;
                                                music.artist = it.artist;
                                                music.artistId = it.artistId;
                                                music.mid = it.songid;
                                                music.coverUri = MusicUtils.getAlbumPic(it.thumb.split("@")[0], Constants.BAIDU, 150);
                                                music.coverSmall = MusicUtils.getAlbumPic(it.thumb.split("@")[0], Constants.BAIDU, 90);
                                                music.coverBig = MusicUtils.getAlbumPic(it.thumb.split("@")[0], Constants.BAIDU, 500);
                                                songs.add(music);
                                            }
                                        }
                                    }
                                    playlist.musicList.addAll(songs);
                                }
                                mvpView.showPlaylistSongs(playlist.musicList);
                            }

                            @Override
                            public void error(String msg) {
                                ToastUtils.getInstance().showToast(msg);
                                System.out.println("***********" + msg);
                            }
                        });
                break;
            case Constants.PLAYLIST_WY_ID:
                ApiManager.request(baiduApiService.getPlaylistDetail(playlist.pid),
                        new RequestCallBack<NeteasePlaylistDetail>() {
                            @Override
                            public void success(NeteasePlaylistDetail result) {
                                if (result.code == 200) {
                                    NewsList playlist = new NewsList();
                                    playlist.pid = String.valueOf(result.playlist.id);
                                    playlist.name = result.playlist.name;
                                    playlist.coverUrl = result.playlist.coverImgUrl;
                                    playlist.des = result.playlist.description;
                                    playlist.date = result.playlist.createTime;
                                    playlist.updateDate = result.playlist.updateTime;
                                    playlist.playCount = result.playlist.playCount;
                                    playlist.type = Constants.PLAYLIST_WY_ID;
                                    playlist.musicList = MusicUtils.getNeteaseMusicList(result.playlist.tracks);
                                    mvpView.showPlaylistSongs(playlist.musicList);
                                }
                            }

                            @Override
                            public void error(String msg) {
                                ToastUtils.getInstance().showToast(msg);
                                System.out.println("***********" + msg);
                            }
                        });
                break;
            default:
                ApiManager.request(baiduApiService.getMusicList(TextUtils.isEmpty(token) ? "" : token, playlist.pid),
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

            ApiManager.request(baiduApiService.getArtistSongList(params),
                    new RequestCallBack<ArtistMusicList>() {
                        @Override
                        public void success(ArtistMusicList result) {
                            Artist artist = new Artist();
                            List<Music> songs = new ArrayList<>();
                            if (result.errorCode == 22000) {
                                for (BaiduMusicList.SongListItem it : result.songList) {
                                    Music music = new Music();
                                    music.type = Constants.BAIDU;
                                    music.title = it.title;
                                    music.artist = it.artistName;
                                    music.artistId = it.tingUid;
                                    music.album = it.albumTitle;
                                    music.albumId = it.albumId;
                                    music.isOnline = true;
                                    music.mid = it.songId;
                                    music.coverUri = MusicUtils.getAlbumPic(it.picSmall.split("@")[0], Constants.BAIDU, 150);
                                    music.coverSmall = MusicUtils.getAlbumPic(it.picSmall.split("@")[0], Constants.BAIDU, 90);
                                    music.coverBig = MusicUtils.getAlbumPic(it.picSmall.split("@")[0], Constants.BAIDU, 500);
                                    songs.add(music);
                                }
                            }

                            artist.count = result.songNums;
                            artist.songs = songs;
                            mvpView.showPlaylistSongs(artist.songs);
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
            ApiManager.request(baiduApiService.getAlbumSongList(album.albumId),
                    new RequestCallBack<AlbumSongList>() {
                        @Override
                        public void success(AlbumSongList result) {
                            Album album = new Album();
                            List<Music> songs = new ArrayList<>();
                            for (BaiduMusicList.SongListItem it : result.songlist) {
                                Music music = new Music();
                                music.type = Constants.BAIDU;
                                music.title = it.title;
                                music.artist = it.artistName;
                                music.artistId = it.tingUid;
                                music.album = it.albumTitle;
                                music.albumId = it.albumId;
                                music.isOnline = true;
                                music.mid = it.songId;
                                music.hasMv = it.hasMv;
                                music.coverUri = MusicUtils.getAlbumPic(it.picSmall.split("@")[0], Constants.BAIDU, 150);
                                music.coverSmall = MusicUtils.getAlbumPic(it.picSmall.split("@")[0], Constants.BAIDU, 90);
                                music.coverBig = MusicUtils.getAlbumPic(it.picSmall.split("@")[0], Constants.BAIDU, 500);
                                songs.add(music);
                            }

                            album.count = result.songlist.size();
                            album.albumId = result.albumInfo.albumId;
                            album.name = result.albumInfo.title;
                            album.artistId = result.albumInfo.artistTingUid;
                            album.artistName = result.albumInfo.author;
                            album.info = result.albumInfo.info;
                            album.songs = songs;

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
