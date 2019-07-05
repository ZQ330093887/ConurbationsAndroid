package com.test.admin.conurbations.model.entity;

public class VideoModel {


    public int status;
    public String user_id;
    public String video_id;
    public double video_duration;


    public VideoListBean video_list;

    public static class VideoListBean {
        public VideoTT video_1;
        public VideoTT video_2;
        public VideoTT video_3;

    }
}
