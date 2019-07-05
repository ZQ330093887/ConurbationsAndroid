package com.test.admin.conurbations.model.entity;

import java.util.List;

public class PageModel extends Base{
    public int id;
    public List<ItemModel> itemList;
    public String nextPage;

    public PageModel(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    public static class ItemModel extends Base{
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

//        @Override
//        public boolean equals(Object o) {
//            if (!(o instanceof ItemModel)) {
//                return false;
//            }
//            ItemModel other = (ItemModel) o;
//            return other.href.equals(this.href);
//        }
    }
}
