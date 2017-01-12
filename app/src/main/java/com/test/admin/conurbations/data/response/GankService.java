package com.test.admin.conurbations.data.response;

import com.test.admin.conurbations.model.NetImage;

import retrofit2.http.GET;
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

}
