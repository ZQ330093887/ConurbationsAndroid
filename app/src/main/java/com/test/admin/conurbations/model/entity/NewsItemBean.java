package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouqiong on 2017/4/11.
 */

public class NewsItemBean implements Serializable {

    /**
     * atype : 1
     * title : 高清-科比拍摄电影宣传片 花絮
     * abstract : 高清-科比拍摄《捉鬼敢死队》宣传片 花絮
     * imgurl : http://inews.gtimg.com/newsapp_ls/0/333429524_640470/0
     * imgurl2 : http://inews.gtimg.com/newsapp_ls/0/333429524_150120/0
     * newsId : 20160603042501
     * url : http://nbachina.qq.com/a/20160603/042501.htm
     * commentId : 1422206477
     * pub_time : 2016-06-03 15:15
     * column : news
     * vid :
     * duration :
     * img_count : 14
     * images_3 : ["http://inews.gtimg.com/newsapp_ls/0/333429524_200160/0","http://inews.gtimg.com/newsapp_ls/0/333429525_200160/0","http://inews.gtimg.com/newsapp_ls/0/333429526_200160/0"]
     * footer : 14图
     */
    public String index;
    public String atype; // 0：新闻  1:多图模式  2:视频 h5:banner
    public String title;
    @SerializedName("abstract")
    public String abstractX;
    public String imgurl;
    public String imgurl2;
    public String newsId;
    public String url;
    public String commentId;
    public String pub_time;
    public String column;
    public String vid;
    public String duration;
    public String img_count;
    public String footer;
    public List<String> images_3;

    public String realUrl; // 存放腾讯视频真实地址

    public NewsItemBean(String imgurl, String title, String pub_time) {
        this.imgurl = imgurl;
        this.title = title;
        this.pub_time = pub_time;
    }
}