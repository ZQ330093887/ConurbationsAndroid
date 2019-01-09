package com.test.admin.conurbations.model.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by ZQiong on 2018/12/20.
 */
public class SearchHistoryBean extends LitePalSupport {

    public long id;
    public String title;

    public SearchHistoryBean(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
