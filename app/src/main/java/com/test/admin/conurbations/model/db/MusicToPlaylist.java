package com.test.admin.conurbations.model.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class MusicToPlaylist extends LitePalSupport {
    public Long id = 0L;
    public String pid = null;
    public String mid = null;
    public Long total = 0L;
    public Long updateDate = 0L;
    public Long createDate = 0L;
}
