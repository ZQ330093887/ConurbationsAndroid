package com.test.admin.conurbations.presenter;

import com.test.admin.conurbations.activitys.IBaiduPlayList;
import com.test.admin.conurbations.activitys.IMvView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.entity.BaiduMusicList;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class PlayListPresenter extends BasePresenter<IBaiduPlayList> {

    @Inject
    public PlayListPresenter() {
    }

    public void loadOnlineMusicList(String type, int limit, int mOffset) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST);
        params.put(Constants.PARAM_TYPE, type);
        params.put(Constants.PARAM_SIZE, String.valueOf(limit));
        params.put(Constants.PARAM_OFFSET, String.valueOf(mOffset));

        ApiManager.request(baiduApiService.getOnlineSongs(params),
                new RequestCallBack<BaiduMusicList>() {
                    @Override
                    public void success(BaiduMusicList result) {
                        List<Music> musicList = new ArrayList<>();
                        if (result != null && result.songList != null) {
                            for (BaiduMusicList.SongListItem songInfo : result.songList) {
                                Music music = new Music();
                                music.type = Constants.BAIDU;
                                music.isOnline = true;
                                music.mid = songInfo.songId;
                                music.album = songInfo.albumTitle;
                                music.albumId = songInfo.albumId;
                                music.artist = songInfo.artistName;
                                music.artistId = songInfo.tingUid;
                                music.title = songInfo.title;
                                music.coverSmall = songInfo.picSmall;
                                music.coverUri = songInfo.picBig;
                                music.coverBig = songInfo.picBig.split("@")[0];
                                musicList.add(music);
                            }
                        }

                        mvpView.showOnlineMusicList(musicList);
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.getInstance().showToast(msg);
                        System.out.println("***********" + msg);
                    }
                });
    }
}
