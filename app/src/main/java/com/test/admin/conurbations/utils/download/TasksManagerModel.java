package com.test.admin.conurbations.utils.download;

import org.litepal.crud.LitePalSupport;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class TasksManagerModel extends LitePalSupport {
    //LitePal自动递增ID
    public int id;
    public int tid;
    public String mid;
    public String name;
    public String url;
    public String path;
    public Boolean finish;
    public boolean cache = true;
}
