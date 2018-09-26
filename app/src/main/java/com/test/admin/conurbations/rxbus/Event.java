package com.test.admin.conurbations.rxbus;

/**
 * Created by ZQiong on 2017/11/1.
 */

public class Event {
    public Object body;
    public EventType eventType;

    public Event(Object body, EventType eventType) {
        this.body = body;
        this.eventType = eventType;
    }
}
