package com.test.admin.conurbations.model.entity;

import java.util.List;

public class ImageEntity {
    public String url;
    public int width;
    public String uri;
    public int height;
    public List<UrlListBeanX> url_list;

    public static class UrlListBeanX {
        public String url;
    }
}
