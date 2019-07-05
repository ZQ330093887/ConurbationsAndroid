package com.test.admin.conurbations.utils;

import com.test.admin.conurbations.model.entity.BaseSoup;

import java.util.Map;

public class SoupFactory {
    public static Map<String, Object> parseHtml(Class<? extends BaseSoup> clazz, String html) {
        return parseHtml(clazz, html, new Object());
    }

    public static Map<String, Object> parseHtml(Class<? extends BaseSoup> clazz, String html, Object... arg) {
        try {
            BaseSoup soup = clazz.getConstructor(String.class).newInstance(html);
            return soup.doParse(arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
