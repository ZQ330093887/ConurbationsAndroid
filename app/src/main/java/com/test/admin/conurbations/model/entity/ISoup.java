package com.test.admin.conurbations.model.entity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

public interface ISoup {
    void parse(Document root, Element head, Element body, Map<String, Object> values);

    Map<String, Object> doParse(Object... arg);
}
