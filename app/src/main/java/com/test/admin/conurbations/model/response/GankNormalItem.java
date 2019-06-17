package com.test.admin.conurbations.model.response;
import com.test.admin.conurbations.model.entity.Gank;

import java.util.ArrayList;
import java.util.List;
public class GankNormalItem extends Gank implements GankItem {

    public int page = -1;

    public static GankNormalItem newGankBean(Gank gank) {
        GankNormalItem gankBean = new GankNormalItem();
        gankBean._id = gank._id;
        gankBean.createdAt = gank.createdAt;
        gankBean.desc = gank.desc;
        gankBean.publishedAt = gank.publishedAt;
        gankBean.source = gank.source;
        gankBean.type = gank.type;
        gankBean.url = gank.url;
        gankBean.who = gank.who;
        gankBean.used = gank.used;
        return gankBean;
    }

    public static List<GankNormalItem> newGankList(List<Gank> gankList) {
        if(null == gankList || gankList.size() == 0) {
            return null;
        }
        List<GankNormalItem> itemList = new ArrayList<>(gankList.size());
        for (Gank gank : gankList) {
            itemList.add(newGankBean(gank));
        }
        return itemList;
    }

    public static List<GankNormalItem> newGankList(List<Gank> gankList, int pageIndex) {
        if(null == gankList || gankList.size() == 0) {
            return null;
        }
        List<GankNormalItem> itemList = new ArrayList<>(gankList.size());
        for (Gank gank : gankList) {
            GankNormalItem item = newGankBean(gank);
            item.page = pageIndex;
            itemList.add(item);
        }
        return itemList;
    }
}
