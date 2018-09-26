package com.test.admin.conurbations.rxbus;

/**
 * RxEvent事件类型
 * Created by ZQiong on 2017/10/17.
 */

public enum EventType {

    STATUE_BAR_COLOR("STATUE_BAR_COLOR"),//状态栏颜色值
    HOME_REFRESH_STORE("HOME_REFRESH_STORE");//

    private String name;

    EventType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
