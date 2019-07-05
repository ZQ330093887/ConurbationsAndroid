package com.test.admin.conurbations.model.entity;

public class MenuModel {
    public int id;
    public String title;
    public String url;

    public MenuModel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MenuModel)) {
            return false;
        }

        MenuModel local = (MenuModel) o;
        return url.equals(local.url);
    }
}
