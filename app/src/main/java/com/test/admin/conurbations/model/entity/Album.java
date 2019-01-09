package com.test.admin.conurbations.model.entity;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZQiong on 2018/12/1.
 */
public class Album extends LitePalSupport implements Serializable {

    public int id;
    public String albumId;
    public String name;
    public String artistName;
    public String cover;
    public String type = Constants.LOCAL;
    public String artistId;
    public String info;
    public int count;

    public List<Music> songs;

    public Album() {
    }

    public Album(String id, String name, String artistName, Long artistId, int count) {
        this.name = name;
        this.albumId = id;
        this.artistName = artistName;
        this.artistId = artistId.toString();
        this.count = count;
    }
}
