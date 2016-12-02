package com.test.admin.conurbations.network;

/**
 * Created by waly6 on 2015/9/25.
 */
public class RequestUrl {

    /**
     分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     请求个数： 数字，大于0
     第几页：数字，大于0
     例：
     http://gank.io/api/data/Android/10/1
     http://gank.io/api/data/福利/10/1
     http://gank.io/api/data/iOS/20/2
     http://gank.io/api/data/all/20/2
     每日数据： http://gank.io/api/day/年/月/日
     例：http://gank.io/api/day/2015/08/06
     随机数据：http://gank.io/api/random/data/分类/个数
     数据类型：福利 | Android | iOS | 休息视频 | 拓展资源 | 前端
     个数： 数字，大于0
     例：http://gank.io/api/random/data/Android/20
     */


    public static String URL_PREFIX = "http://gank.io/api/";
    public static String URL_DATA_FULI = "data/福利/10/1";

}
