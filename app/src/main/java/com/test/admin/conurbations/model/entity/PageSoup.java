package com.test.admin.conurbations.model.entity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageSoup extends MenuSoup {
    private static final String TAG = "PageSoup";

    public PageSoup(String html) {
        super(html);
    }

    @Override
    public void parse(Document root, Element head, Element body, Map<String, Object> values) {
        super.parse(root, head, body, values);


        Element element = body.getElementById("pins");
        if (element != null) {
            Elements li = element.getElementsByTag("li");
            List<PageModel.ItemModel> value = new ArrayList<>();
            PageModel pageModel = new PageModel(value);
            for (Element litag : li) {
                String fengmianUrl = litag.getElementsByTag("a").first().getElementsByTag("img").first().attr("data-original");
                String title = litag.getElementsByTag("a").first().getElementsByTag("img").first().attr("alt");
                String href = litag.getElementsByTag("a").first().attr("href");
                String time = litag.getElementsByTag("span").get(1).text();
                String viewerNum = litag.getElementsByTag("span").get(1).text();

                PageModel.ItemModel model = new PageModel.ItemModel(href, title, fengmianUrl);
                value.add(model);
            }
            values.put(getClass().getSimpleName(), pageModel);
        } else {
            Elements li = body.getElementsByClass("comment-body");
            List<PageModel.ItemModel> value = new ArrayList<>();
            PageModel pageModel = new PageModel(value);
            for (Element litag : li) {
                String fengmianUrl = litag.getElementsByTag("img").first().attr("data-original");
                String title = litag.getElementsByTag("a").first().text();
                String href = litag.getElementsByTag("a").first().attr("href");

                PageModel.ItemModel model = new PageModel.ItemModel(href, title, fengmianUrl);
                value.add(model);
            }
            values.put(getClass().getSimpleName(), pageModel);
        }
    }
}

