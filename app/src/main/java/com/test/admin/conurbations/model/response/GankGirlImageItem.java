package com.test.admin.conurbations.model.response;
import com.test.admin.conurbations.model.entity.Gank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GankGirlImageItem implements GankItem {

    public String imgUrl;
    public Date publishedAt;

    public static GankGirlImageItem newImageItem(Gank gank) {
        GankGirlImageItem imageItem = new GankGirlImageItem();
        imageItem.imgUrl = gank.url;
        imageItem.publishedAt = gank.publishedAt;
        return imageItem;
    }

    public static List<GankGirlImageItem> newImageList(List<Gank> gankList) {
        if (null == gankList || gankList.size() == 0) {
            return null;
        }
        List<GankGirlImageItem> itemList = new ArrayList<>(gankList.size());
        for (Gank gank : gankList) {
            itemList.add(newImageItem(gank));
        }
        return itemList;
    }
}
