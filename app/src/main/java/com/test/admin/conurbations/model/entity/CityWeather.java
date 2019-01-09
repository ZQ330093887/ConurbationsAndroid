package com.test.admin.conurbations.model.entity;

import java.util.List;

/**
 * Created by ZQiong on 2019/1/9.
 */
public class CityWeather {

    /**
     * error : 0
     * status : success
     * date : 2019-01-09
     * results : [{"currentCity":"长沙","pm25":"19","index":[{"des":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。","tipt":"穿衣指数","title":"穿衣","zs":"冷"},{"des":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。","tipt":"洗车指数","title":"洗车","zs":"不宜"},{"des":"天冷空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。","tipt":"感冒指数","title":"感冒","zs":"易发"},{"des":"有降水，推荐您在室内进行各种健身休闲运动，若坚持户外运动，须注意保暖并携带雨具。","tipt":"运动指数","title":"运动","zs":"较不宜"},{"des":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。","tipt":"紫外线强度指数","title":"紫外线强度","zs":"最弱"}],"weather_data":[{"date":"周三 01月09日 (实时：3℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/xiaoyu.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/xiaoyu.png","weather":"小雨","wind":"北风微风","temperature":"5 ~ 2℃"},{"date":"周四","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/zhongyu.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/zhongyu.png","weather":"中雨","wind":"北风微风","temperature":"5 ~ 3℃"},{"date":"周五","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/xiaoyu.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"小雨转阴","wind":"西北风微风","temperature":"6 ~ 2℃"},{"date":"周六","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/yin.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"阴","wind":"北风微风","temperature":"8 ~ 2℃"}]}]
     */

    public int error;
    public String status;
    public String date;
    public List<ResultsBean> results;

    public static class ResultsBean {
        /**
         * currentCity : 长沙
         * pm25 : 19
         * index : [{"des":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。","tipt":"穿衣指数","title":"穿衣","zs":"冷"},{"des":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。","tipt":"洗车指数","title":"洗车","zs":"不宜"},{"des":"天冷空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。","tipt":"感冒指数","title":"感冒","zs":"易发"},{"des":"有降水，推荐您在室内进行各种健身休闲运动，若坚持户外运动，须注意保暖并携带雨具。","tipt":"运动指数","title":"运动","zs":"较不宜"},{"des":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。","tipt":"紫外线强度指数","title":"紫外线强度","zs":"最弱"}]
         * weather_data : [{"date":"周三 01月09日 (实时：3℃)","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/xiaoyu.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/xiaoyu.png","weather":"小雨","wind":"北风微风","temperature":"5 ~ 2℃"},{"date":"周四","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/zhongyu.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/zhongyu.png","weather":"中雨","wind":"北风微风","temperature":"5 ~ 3℃"},{"date":"周五","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/xiaoyu.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"小雨转阴","wind":"西北风微风","temperature":"6 ~ 2℃"},{"date":"周六","dayPictureUrl":"http://api.map.baidu.com/images/weather/day/yin.png","nightPictureUrl":"http://api.map.baidu.com/images/weather/night/yin.png","weather":"阴","wind":"北风微风","temperature":"8 ~ 2℃"}]
         */

        public String currentCity;
        public String pm25;
        public List<IndexBean> index;
        public List<WeatherDataBean> weather_data;

        public static class IndexBean {
            /**
             * des : 天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。
             * tipt : 穿衣指数
             * title : 穿衣
             * zs : 冷
             */

            public String des;
            public String tipt;
            public String title;
            public String zs;
        }

        public static class WeatherDataBean {
            /**
             * date : 周三 01月09日 (实时：3℃)
             * dayPictureUrl : http://api.map.baidu.com/images/weather/day/xiaoyu.png
             * nightPictureUrl : http://api.map.baidu.com/images/weather/night/xiaoyu.png
             * weather : 小雨
             * wind : 北风微风
             * temperature : 5 ~ 2℃
             */

            public String date;
            public String dayPictureUrl;
            public String nightPictureUrl;
            public String weather;
            public String wind;
            public String temperature;
        }
    }
}
