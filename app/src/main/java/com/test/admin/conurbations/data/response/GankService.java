package com.test.admin.conurbations.data.response;

import com.test.admin.conurbations.model.NetImage;
import com.test.admin.conurbations.model.NetImage360;
import com.test.admin.conurbations.model.NewsDetail;
import com.test.admin.conurbations.model.NewsList;

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

}
