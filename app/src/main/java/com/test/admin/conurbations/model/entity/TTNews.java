package com.test.admin.conurbations.model.entity;

import java.util.List;

public class TTNews {
    public int article_type;
    public String tag;
    public String title;
    public int hot;
    public String source;
    public int comment_count;
    public String article_url;
    public int gallary_image_count;
    public int video_style;
    public String item_id;
    public long behot_time;
    public String url;
    public boolean has_image;
    public boolean has_video;
    public int video_duration;
    public VideoEntity video_detail_info;
    public String group_id;
    public ImageEntity middle_image;
    public List<ImageEntity> image_list;
}
