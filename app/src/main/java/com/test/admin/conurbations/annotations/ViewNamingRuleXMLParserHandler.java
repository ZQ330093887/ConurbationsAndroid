package com.test.admin.conurbations.annotations;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouqiong on 2017/4/1.
 */
public class ViewNamingRuleXMLParserHandler extends DefaultHandler {

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("about")) {
            ViewNamingRuleConfig.VERSION = attributes.getValue(0);
            ViewNamingRuleConfig.AUTHOR = attributes.getValue(1);
        }
        else if (qName.equals("containers")) {
            ViewNamingRuleConfig.CONTAINERS = new ArrayList<>();
        }
        else if (qName.equals("container")) {
            ViewNamingRuleConfig.CONTAINERS.add(attributes.getValue(0));
        }
        else if (qName.equals("views")) {
            ViewNamingRuleConfig.VIEWS_ABBREV = new HashMap<>();
        }
        else if (qName.equals("view")) {
            ViewNamingRuleConfig.VIEWS_ABBREV.put(attributes.getValue(0), attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }
}
