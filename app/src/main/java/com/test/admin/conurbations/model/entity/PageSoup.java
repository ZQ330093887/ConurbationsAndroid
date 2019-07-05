package com.test.admin.conurbations.model.entity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
        Element child = body.getElementById("content");
        if (child == null) return;
        Elements children = child.getElementsByTag("figure");
        List<PageModel.ItemModel> value = new ArrayList<>();
        PageModel pageModel = new PageModel(value);
        if (children != null && children.size() > 0) {
            for (Element localChild : children) {
                List<Node> nodes = localChild.childNodes();
                Node item = nodes.get(1);
                String href = item.attr("href");
                String des = item.attr("title");
                String imgUrl = item.childNode(0).attr("data-original");
                PageModel.ItemModel model = new PageModel.ItemModel(href, des, imgUrl);
                value.add(model);
            }

            Element element = root.getElementById("pagebtn");
            Elements elements = element.getElementsByTag("a");
            for (int i = 0; i < elements.size(); i++) {
                Element ele = elements.get(i);
                if (ele.text().contains("下一页")) {
                    pageModel.nextPage = ele.attr("href");
                    break;
                }
            }
            values.put(getClass().getSimpleName(), pageModel);
            return;
        }

        children = child.getElementsByTag("p");
        //自拍
        if (children != null && children.size() > 0) {
            for (Element localChild : children) {
                Element element = localChild.getElementsByTag("img").get(0);
                String url = element.attr("src");
                String des = element.attr("alt");
                PageModel.ItemModel model = new PageModel.ItemModel(url.substring(url.lastIndexOf("/") + 1), des, url);
                value.add(model);
            }

            Elements childs = child.getElementsByClass("prev-next");
            if (childs != null && childs.size() > 0) {
                childs = childs.get(0).getElementsByTag("a");
                for (int i = 0; i < childs.size(); i++) {
                    Element ele = childs.get(i);
                    if (ele.text().contains("下一页")) {
                        pageModel.nextPage = ele.attr("href");
                        break;
                    }
                }
            }
            values.put(getClass().getSimpleName(), pageModel);
            return;
        }
//        else {
//            children = child.getElementsByClass("archive-brick");
//            if (children != null && children.size() > 0) {
//                for (Element localChild : children) {
//
//                    Element element = localChild.getElementsByTag("a").get(0);
//                    String url = element.attr("href");
//                    String des = element.childNodes().get(1).toString();
//                    PageModel.ItemModel model = new PageModel.ItemModel(url.substring(url.lastIndexOf("/") + 1), des, url+"/");
//                    value.add(model);
//                }
//                values.put(getClass().getSimpleName(), pageModel);
//                return;
//            }
//        }
    }
}

