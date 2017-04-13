package com.test.admin.conurbations.model.api;

import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.model.entity.NewsIndex;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.model.response.TodayData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 */
public interface GankService {

    @GET("data/{category}/{pageCount}/{page}")
    Observable<GankData> getGank(@Path("category") String category, @Path("pageCount") int pageCount, @Path("page") int page);

    /**
     * 获取某天的干货
     */

    @GET("day/{year}/{month}/{day}")
    Observable<TodayData> getDayGank(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    /***
     * 根据类别查询干货
     */
    @GET("data/{category}/20/{pageIndex}")
    Observable<GankData> getGanHuo(@Path("category") String category
            , @Path("pageIndex") int pageIndex);

    /**
     * http://pic.sogou.com/pics?query="+word+"&start="+page*24+"&reqType=ajax&reqFrom=result
     */

    @GET("http://pic.sogou.com/pics")
    Observable<NetImage> getImageList(
            @Query("reqType") String ajax,
            @Query("reqFrom") String result,
            @Query("query") String word,
            @Query("start") int page);


    /**
     * http://wallpaper.apc.360.cn/index.php?c=WallPaperAloneRelease&a=getAppsByRecommWithTopic&order=create_time&start=0&count=20
     */

    @GET("http://wallpaper.apc.360.cn/index.php?c=WallPaperAloneRelease&a=getAppsByRecommWithTopic&order=create_time&start=20")
    Observable<NetImage360> get360ImageList(@Query("count") int count);


    /**
     * http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAppsByCategory&cid=11&start=0&count=99
     **/

    @GET("http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAppsByCategory&start=0&count=99")
    Observable<NetImage360> get360ImageItemList(@Query("cid") String cid);


    @Headers("Cache-Control: public, max-age=60")
    @GET("http://news-at.zhihu.com/api/4/stories/latest")
    Observable<NewsList> getLatestNews();

    @Headers("Cache-Control: public, max-age=" + 60 * 60 * 24 * 7)
    @GET("http://news-at.zhihu.com/api/4/stories/before/{date}")
    Observable<NewsList> getBeforeNews(@Path("date") String date);

    @Headers("Cache-Control: public, max-age=" + 60 * 60 * 24 * 7)
    @GET("http://news-at.zhihu.com/api/4/story/{id}")
    Observable<NewsDetail> getNewsDetail(@Path("id") int id);

    /**
     * 获取主题日报内容列表
     * @param themeId 主题日报id
     */
    @GET("http://news-at.zhihu.com/api/4/theme/{themeId}")
    Observable<NewsList> getThemeContentList(@Path("themeId") String themeId);

    /**
     * NBA
     * */
    @GET("http://sportsnba.qq.com/news/index")
    Observable<NewsIndex> getNewsIndex(@Query("column") String column);

    @GET("http://sportsnba.qq.com/news/item")
    Call<String> getNewsItem(@Query("column") String column, @Query("articleIds") String articleIds);

    @GET("http://sportsnba.qq.com/news/detail")
    Call<String> getNewsDetail(@Query("column") String column, @Query("articleId") String articleId);

    /**
     * 最新方法
     * http://h5vv.video.qq.com/getinfo?callback=tvp_request_getinfo_callback_340380&platform=11001&charge=0&otype=json&ehost=http%3A%2F%2Fv.qq.com&sphls=0&sb=1&nocache=0&_rnd=1474896074003&vids=m0022ect1qs&defaultfmt=auto&&_qv_rmt=CTWS8OLbA17867igt=&_qv_rmt2=x6oMojAw144904luQ=&sdtfrom=v3010&callback=tvp_request_getinfo_callback_
     *
     * @param vids
     * @return
     */
    @GET("getinfo?platform=11001&charge=0&otype=json")
    Call<String> getVideoRealUrls(@Query("vids") String vids);
}
