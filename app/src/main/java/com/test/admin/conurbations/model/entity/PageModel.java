package com.test.admin.conurbations.model.entity;

import java.util.List;
import java.util.Objects;

public class PageModel extends Base {
    public int id;
    public List<ItemModel> itemList;
    public String nextPage;

    public int index;//本地数据传输用

    public PageModel(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    public static class ItemModel extends Base {
        public int id;
        public String href;
        public String description;
        public String imgUrl;
        public int height;

        public ItemModel(String href, String description, String imgUrl) {
            this.href = href;
            this.description = description;
            this.imgUrl = imgUrl;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageModel pageModel = (PageModel) o;
        return nextPage.equals(pageModel.nextPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextPage);
    }
}
