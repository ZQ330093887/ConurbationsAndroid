package com.test.admin.conurbations.menus;

/**
 * Created by zhouqiong on 2016/1/14.
 */
public class MenuItem {

    public MenuItem(String text, boolean isSelected, int icon, int iconSelected) {
        this.text = text;
        this.isSelected = isSelected;
        this.icon = icon;
        this.iconSelected = iconSelected;
    }

    boolean isSelected;
    String text;
    int icon;
    int iconSelected;
}