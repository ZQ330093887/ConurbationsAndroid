package com.test.admin.conurbations.model.response;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 2019/6/11.
 */
public class GankImageData implements GankItem {
    public List<GankGirlImageItem> imageItem;


    public static GankImageData newImageList(List<GankGirlImageItem> gankList) {
        if (null == gankList || gankList.size() == 0) {
            return null;
        }
        GankImageData imageData = new GankImageData();
        imageData.imageItem = new ArrayList<>(gankList.size());
        imageData.imageItem.clear();
        imageData.imageItem.addAll(gankList);
        return imageData;
    }

}
