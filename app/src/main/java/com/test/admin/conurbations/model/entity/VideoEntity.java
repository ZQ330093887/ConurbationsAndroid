package com.test.admin.conurbations.model.entity;

import java.util.List;

public class VideoEntity {
    public int group_flags;
    public int video_type;
    public int video_preloading_flag;
    public int direct_play;
    public ImageEntity detail_video_large_image;
    public int show_pgc_subscribe;
    public String video_third_monitor_url;
    public String video_id;
    public int video_watching_count;
    public int video_watch_count;
    public List<?> video_url;
    //自己新增的字段，记录视频播放的进度，用于同步视频列表也和详情页的进度
    public long progress;
    public String parse_video_url; //解析出来的视频地址
}
