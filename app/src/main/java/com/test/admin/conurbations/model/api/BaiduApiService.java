package com.test.admin.conurbations.model.api;

import com.test.admin.conurbations.model.MusicInfo;
import com.test.admin.conurbations.model.entity.AlbumSongList;
import com.test.admin.conurbations.model.entity.ArtistMusicList;
import com.test.admin.conurbations.model.entity.BaiduList;
import com.test.admin.conurbations.model.entity.BaiduMusicList;
import com.test.admin.conurbations.model.entity.BaiduSearchMergeInfo;
import com.test.admin.conurbations.model.entity.BaiduSongInfo;
import com.test.admin.conurbations.model.entity.Candidates;
import com.test.admin.conurbations.model.entity.DoubanMusic;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.model.entity.NeteasePlaylistDetail;
import com.test.admin.conurbations.model.entity.PlaylistInfo;
import com.test.admin.conurbations.model.entity.RadioChannelData;
import com.test.admin.conurbations.model.entity.SearchInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by ZQiong on 2018/11/29.
 */
public interface BaiduApiService {

    @GET("v1/restserver/ting?")
    Observable<BaiduList> getOnlinePlaylist(@QueryMap Map<String, String> params);

    /**
     * 获取网易云排行榜
     *
     * @return
     */
    @GET("music/netease/rank")
    Observable<List<PlaylistInfo>> getNeteaseRank(@Query("ids[]") int[] ids, @Query("limit") Integer limit);


    /**
     * 获取网易云排行榜
     *
     * @return
     */
    @GET("music/qq/rank")
    Observable<List<PlaylistInfo>> getQQRank(@Query("limit") Integer limit, @Query("ids[]") int[] ids);



    /**
     * 获取歌单
     *
     * @param token 秘钥
     * @return
     */
    @GET("playlist")
    Observable<ResponseBody> getOnlinePlaylists(@Header("accesstoken") String token);


    @GET("v1/restserver/ting?")
    Observable<BaiduMusicList> getOnlineSongs(@QueryMap Map<String, String> params);

    /**
     * 获取最新mv
     */
    @GET("/mv/first")
    Observable<MvInfo> getNewestMv(@Query("limit") int limit);

    /**
     * 获取mv排行榜
     */
    @GET("/top/mv")
    Observable<MvInfo> getTopMv(@Query("offset") int offset, @Query("limit") int limit);

    @GET("v2/music/{method}")
    Observable<DoubanMusic> searchMusic(@Path("method") String method, @QueryMap Map<String, String> params);

    @GET()
    Observable<BaiduSongInfo> getTingSongInfo(@Url String baseUrl);

    @GET("/v1/restserver/ting?from=qianqian&version=5.6.5.0&method=baidu.ting.radio.getChannelSong&format=json&pn=0&rn=10")
    Observable<RadioChannelData> getRadioChannelSongs(@Query("channelname") String channelName);

    @GET("/playlist/detail")
    Observable<NeteasePlaylistDetail> getPlaylistDetail(@Query("id") String id);

    /**
     * 获取歌单数据
     *
     * @param token 秘钥
     * @param id    歌单id
     * @return
     */
    @GET("playlist/{id}")
    Observable<ResponseBody> getMusicList(@Header("accesstoken") String token, @Path("id") String id);

    /**
     * 获取歌手歌曲信息
     */
    @GET("/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.artist.getSongList&format=json")
    Observable<ArtistMusicList> getArtistSongList(@QueryMap HashMap<String, Object> params);

    /**
     * 获取专辑信息
     */
    @GET("/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json")
    Observable<AlbumSongList> getAlbumSongList(@Query("album_id") String albumId);

    @GET
    Observable<ResponseBody> getBaiduLyric(@Url String baseUrl);

    @GET("search?ver=1&man=yes&client=pc")
    Observable<Candidates.KugouLyric> searchLyric(@Query("keyword") String songName, @Query("duration") String duration);

    @GET("download?ver=1&client=pc&fmt=lrc&charset=utf8")
    Observable<Candidates.KugouLyricInfo> getRawLyric(@Query("id") String id, @Query("accesskey") String accesskey);

    /**
     * 删除歌单
     *
     * @param token 秘钥
     * @param id    歌单id
     * @return
     */
    @DELETE("playlist")
    Observable<ResponseBody> deleteMusic(@Header("accesstoken") String token, @Query("id") String id);

    /**
     * 取消收藏歌曲
     *
     * @param token 秘钥
     * @return
     */
    @DELETE("playlist/{id}")
    Observable<ResponseBody> disCollectMusic(@Header("accesstoken") String token, @Path("id") String id, @Query("id") String songid);


    /**
     * 重命名歌单
     *
     * @param token    秘钥
     * @param id       歌单id
     * @param playlist 歌单信息
     * @return
     */
    @PUT("playlist/{id}")
    @Headers("Content-Type: application/json")
    Observable<ResponseBody> renameMusic(@Header("accesstoken") String token, @Path("id") String id, @Body PlaylistInfo playlist);

    /**
     * 收藏歌曲
     *
     * @param token
     * @param id        歌单id
     * @param musicInfo 歌曲信息
     * @return
     */
    @POST("playlist/{id}")
    @Headers("Content-Type: application/json")
    Observable<ResponseBody> collectMusic(@Header("accesstoken") String token, @Path("id") String id, @Body MusicInfo musicInfo);

    /**
     * 批量收藏歌曲(不同音乐源)
     *
     * @param token
     * @param ids       歌单ids
     * @param musicInfo 歌曲信息
     * @return
     */
    @POST("playlist/{id}/batch2")
    @Headers("Content-Type: application/json")
    Observable<MusicInfo.CollectResult> collectBatch2Music(@Header("accesstoken") String token, @Path("id") String id, @Body Object data);

    /**
     * 搜索
     */
    @GET("$V1_TING?method=$QUERY_MERGE")
    Observable<BaiduSearchMergeInfo> queryMerge(@Query("query") String query,
                                                @Query("page_no") int pageNo,
                                                @Query("page_size") int pageSize);

    @GET("search/hot")
    Observable<SearchInfo> getHotSearchInfo();

}
