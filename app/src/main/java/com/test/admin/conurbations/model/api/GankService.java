package com.test.admin.conurbations.model.api;

import com.test.admin.conurbations.model.entity.CityWeather;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.model.entity.NewsIndex;
import com.test.admin.conurbations.model.entity.NewsResponse;
import com.test.admin.conurbations.model.entity.VideoModel;
import com.test.admin.conurbations.model.response.GankData;
import com.test.admin.conurbations.model.response.GankHotData;
import com.test.admin.conurbations.model.response.GankImageData;
import com.test.admin.conurbations.model.response.NetImage;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.model.response.ResultResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GankService {
    /**
     * 本周最热 API
     */
    @GET("hot/views/category/GanHuo/count/10")
    Observable<GankHotData> getDayGank();

    /**
     * 获取banner
     * https://gank.io/api/v2/banners
     */
    @GET("banners")
    Observable<GankImageData> getBanners();

    /***
     * 根据类别查询干货
     * data/category/GanHuo/type/Android/page/1/count/10
     * data/category/GanHuo/type/all/page/1/count/10
     */
    @GET("data/category/{category}/type/{type}/page/{pageIndex}/count/10")
    Observable<GankData> getGanHuo(@Path("category") String category, @Path("type") String type
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

    @Headers("Cache-Control: public, max-age=" + 60 * 60 * 24 * 7)
    @GET("http://news-at.zhihu.com/api/4/story/{id}")
    Observable<NewsDetail> getNewsDetail(@Path("id") int id);

    /**
     * NBA
     */
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
     * @return http://h5vv.video.qq.com/getinfo?&vids=m0022ect1qs
     */
    @GET("getinfo?platform=11001&charge=0&otype=json&ehost=http%3A%2F%2Fv.qq.com&sphls=0")
    Call<String> getVideoRealUrls(@Query("vids") String vids);

    /**
     * http://api.map.baidu.com/telematics/v3/weather?location=北京&output=json&ak=vZlRYC39tTuniYzNcX2zrQmZzblZcXwp
     *
     * @param location 地区
     * @return 天气实体
     */
    @GET("http://api.map.baidu.com/telematics/v3/weather?output=json&ak=vZlRYC39tTuniYzNcX2zrQmZzblZcXwp")
    Observable<CityWeather> getCityWeather(@Query("location") String location);


    //今日头条
    String GET_ARTICLE_LIST = "http://is.snssdk.com/api/news/feed/v62/?refer=1&count=20&loc_mode=4&device_id=34960436458&iid=13136511752";

    /**
     * 获取新闻列表
     * Request{method=GET, url=http://is.snssdk.com/api/news/feed/v62/?refer=1&count=20&loc_mode=4&device_id=34960436458&iid=13136511752&category=subv_funny&min_behot_time=1561542331&last_refresh_sub_entrance_interval=1562227787, tag=null}
     *
     * @param category 频道
     * @return
     */
    @GET(GET_ARTICLE_LIST)
    Observable<NewsResponse> getNewsList(@Query("category") String category, @Query("min_behot_time") long lastTime, @Query("last_refresh_sub_entrance_interval") long currentTime);


    /**
     * 获取视频页的html代码
     */
    @GET
    Observable<String> getVideoHtml(@Url String url);

    /**
     * 获取视频数据json
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResultResponse<VideoModel>> getVideoData(@Url String url);
}
