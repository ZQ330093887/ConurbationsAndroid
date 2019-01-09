package com.test.admin.conurbations.rxbus;

/**
 * Created by ZQiong on 2017/11/1.
 */

public class Event {
    public Object body;
    public String eventType;

    public Event(Object body, String eventType) {
        this.body = body;
        this.eventType = eventType;
    }
}
