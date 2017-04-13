package com.test.admin.conurbations.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.test.admin.conurbations.model.entity.Base;
import com.test.admin.conurbations.model.entity.NewsDetail;
import com.test.admin.conurbations.model.entity.NewsItem;
import com.test.admin.conurbations.model.entity.NewsItemBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonParserUtil {

    public static String parseBase(Base base, String jsonStr) {
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        String data = "{}";
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            if (entry.getKey().equals("code")) {
                base.code = Integer.parseInt(entry.getValue().toString());
            } else if (entry.getKey().equals("version")) {
                base.version = entry.getValue().toString();
            } else {
                data = entry.getValue().toString();
            }
        }
        return data;
    }

    public static NewsItem parseNewsItem(String jsonStr) {
        NewsItem newsItem = new NewsItem();
        JSONObject data = JSON.parseObject(JsonParserUtil.parseBase(newsItem, jsonStr)); // articleIds=    NullPoint
        List<NewsItemBean> list = new ArrayList<>();
        //Set<String> keySet = data.keySet();
        for (Map.Entry<String, Object> item : data.entrySet()) {
            Gson gson = new Gson();
            NewsItemBean bean = gson.fromJson(item.getValue().toString(), NewsItemBean.class);
            bean.index = item.getKey();
            list.add(bean);
        }
        // 由于fastjson获取出来的entrySet是乱序的  所以这边重新排序
        Collections.sort(list, new Comparator<NewsItemBean>() {
            @Override
            public int compare(NewsItemBean lhs, NewsItemBean rhs) {
                return rhs.index.compareTo(lhs.index);
            }
        });
        newsItem.data = list;
        return newsItem;
    }

    public static NewsDetail parseNewsDetail(String jsonStr) {
        NewsDetail detail = new NewsDetail();
        String dataStr = JsonParserUtil.parseBase(detail, jsonStr);
        JSONObject data = JSON.parseObject(dataStr);
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getKey().equals("title")) {
                    detail.title = entry.getValue().toString();
                } else if (entry.getKey().equals("abstract")) {
                    detail.abstractX = entry.getValue().toString();
                } else if (entry.getKey().equals("content")) {
                    String contentStr = entry.getValue().toString();
                    try {
                        List<Map<String, String>> list = new LinkedList<>();
                        JSONArray jsonArray = new JSONArray(contentStr);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象
                            Map<String, String> map = new HashMap<>();
                            if (item.get("type").equals("text")) {
                                map.put("text", item.get("info").toString());
                            } else if (item.get("type").equals("img")) {
                                String imgStr = item.get("img").toString();
                                JSONObject imgObj = JSON.parseObject(imgStr);
                                for (Map.Entry<String, Object> imgItem : imgObj.entrySet()) {
                                    if (imgItem.getKey().toString().startsWith("imgurl") && !TextUtils.isEmpty(imgItem.getValue().toString())) {
                                        JSONObject imgUrlObj = JSON.parseObject(imgItem.getValue().toString());
                                        String url = imgUrlObj.getString("imgurl");
                                        map.put("img", url);
                                        break;
                                    }
                                }
                            }
                            list.add(map);
                        }
                        detail.content = list;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (entry.getKey().equals("url")) {
                    detail.url = entry.getValue().toString();
                } else if (entry.getKey().equals("imgurl")) {
                    detail.imgurl = entry.getValue().toString();
                } else if (entry.getKey().equals("imgurl1")) {
                    detail.imgurl1 = entry.getValue().toString();
                } else if (entry.getKey().equals("imgurl2")) {
                    detail.imgurl2 = entry.getValue().toString();
                } else if (entry.getKey().equals("pub_time")) {
                    detail.time = entry.getValue().toString();
                } else if (entry.getKey().equals("atype")) {
                    detail.atype = entry.getValue().toString();
                } else if (entry.getKey().equals("commentId")) {
                    detail.commentId = entry.getValue().toString();
                } else {
                    detail.newsAppId = entry.getValue().toString();
                }
            }
        }
        return detail;
    }
}
