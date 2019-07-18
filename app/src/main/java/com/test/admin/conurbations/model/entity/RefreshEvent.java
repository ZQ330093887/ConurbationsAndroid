package com.test.admin.conurbations.model.entity;

import java.io.Serializable;

public class RefreshEvent implements Serializable {
    int position;
    long max_cursor;

    public RefreshEvent(int position, long max_cursor) {
        this.position = position;
        this.max_cursor = max_cursor;
    }

    public int getPosition() {
        return position;
    }

    public long getMaxCursor() {
        return max_cursor;
    }
}
