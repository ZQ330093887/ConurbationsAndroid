package com.test.admin.conurbations.config;

import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhouqiong on 2016/3/15.
 */
public class Constants {

    // 创建一个以当前时间为名称的文件
    public static final String pathFileName = Environment.getExternalStorageDirectory() + "/" + getPhotoFileName();

    public static final String DATA_CACHE_FOLDER_PATH = "/wenjianhuancun/data";

    public static final String IMAGE_CACHE_FOLDER_PATH = "/tupianhuancun/imgs";

    public static final String DEAULT_NOTIFICATION = "notification";
    public static final String STATUE_BAR_COLOR = "STATUE_BAR_COLOR";//修改颜色

    public static final String IS_URL_HEADER = "http";

    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照

    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static final int[] testColors = {0xFFc72e45, 0xFF009688, 0xFF607D8B, 0xFF5A9367};

    public static final int[] toolBarColors = {0xFFa6172d, 0xFF00796B, 0xFF455A64, 0xFF44633F};
    //百度歌单
    public static final String PLAYLIST_BD_ID = "playlist_bd";
    //网易云歌单
    public static final String PLAYLIST_WY_ID = "playlist_wy";
    //QQ歌单
    public static final String PLAYLIST_QQ_ID = "playlist_qq";
    //在线歌单
    public static final String PLAYLIST_CUSTOM_ID = "custom_online";
    public static final String PLAYLIST_SEARCH_ID = "playlist_search";
    public static final String PLAYLIST_IMPORT_ID = "playlist_import";

    //特殊歌单类型
    public static final String PLAYLIST_LOVE_ID = "love";
    public static final String PLAYLIST_HISTORY_ID = "history";
    public static final String PLAYLIST_LOCAL_ID = "local";
    public static final String PLAYLIST_QUEUE_ID = "queue";
    public static final String PLAYLIST_DOWNLOAD_ID = "download";

    //歌单操作
    public static final int PLAYLIST_ADD = 0;
    public static final int PLAYLIST_DELETE = 1;
    public static final int PLAYLIST_UPDATE = 2;
    public static final int PLAYLIST_RENAME = 3;


    //歌曲类型
    public static final String LOCAL = "local";
    public static final String QQ = "qq";
    public static final String XIAMI = "xiami";
    public static final String BAIDU = "baidu";
    public static final String NETEASE = "netease";

    public static final String PARAM_METHOD = "method";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_PAGE_SIZE = "page_size";
    public static final String PARAM_PAGE_NO = "page_no";
    public static final String PARAM_OFFSET = "offset";
    public static final String PARAM_LIMIT = "limit";
    public static final String WEATHER = "weather";
    public static final String PARAM_TING_UID = "tinguid";
    public static final String PARAM_ALBUM_ID = "album_id";
    public static final String PARAM_QUERY = "query";

    public static final String SP_KEY_SONG_QUALITY = "song_quality";
    public static final String META_CHANGED_EVENT = "meta_changed_event";//播放状态改版
    public static final String STATUS_CHANGED_EVENT = "status_changed_event";//播放暂停广播
    public static final String DOWNLOAD_EVENT = "DOWNLOAD_EVENT";//下载任务唯一ID
    public static final String URL_GET_SONG_INFO = "http://music.baidu.com/data/music/links?songIds=";

    /**
     * 虾米音乐Api*************************************************
     */
    public static final String BASE_XIAMI_URL = "http://api.xiami.com/";
    /**
     * 酷狗音乐Api*************************************************
     */
    public static final String BASE_KUGOU_URL = "http://lyrics.kugou.com/";
    /**
     * 百度音乐Api*************************************************
     */
    public static final String BASE_URL_BAIDU_MUSIC = "http://musicapi.qianqian.com/";
    public static final String METHOD_CATEGORY = "baidu.ting.billboard.billCategory";
    public static final String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    public static final String METHOD_SEARCH_SUGGESTION = "baidu.ting.search.suggestion";
    public static final String METHOD_MUSIC_INFO = "baidu.ting.song.getInfos";
    public static final String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    public static final String METHOD_SEARCH_MUSIC = "baidu.ting.search.common";
    public static final String METHOD_SEARCH_MERGE = "baidu.ting.search.merge";

    /**
     * 在线歌单接口Api*************************************************
     */

    public static final String BASE_PLAYER_URL = "https://player.zzsun.cc";
    public static final String BASE_TEST_PLAYER_URL = "https://player-test.zzsun.cc/";
    //    public static final String BASE_NETEASE_URL = "http://192.168.123.44:3000";
    public static final String BASE_NETEASE_URL = "https://netease.api.zzsun.cc/";
    //    public static final String BASE_NETEASE_URL = "http://musicapi.leanapp.cn/";
    //bugly app_id
    public static final String BUG_APP_ID = "fd892b37ea";


    //更新用户信息

    public static final String UPDATE_USER = "updateUserInfo";

    //用户id
    public static final String USER_ID = "user_id";
    //用户邮箱
    public static final String USER_EMAIL = "email";
    //用户登录密码
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String TOKEN_TIME = "token_time";
    public static final String LOGIN_STATUS = "login_status";
    //用户名
    public static final String USERNAME = "username";
    //性别
    public static final String USER_SEX = "user_sex";
    //性别
    public static final String USER_IMG = "user_img";
    public static final String USER_COLLEGE = "user_college";
    public static final String USER_MAJOR = "user_major";
    public static final String USER_CLASS = "user_class";
    public static final String NICK = "nick";
    public static final String PHONE = "phone";
    public static final String SECRET = "secret";

    /**
     * 悬浮窗权限requestCode
     */
    public static final int REQUEST_CODE_FLOAT_WINDOW = 0x123;

    // 使用系统当前日期加以调整作为照片的名称
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd");
        return dateFormat.format(date) + ".jpg";
    }
}
