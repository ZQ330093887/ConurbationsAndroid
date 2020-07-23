package com.test.admin.conurbations.model.entity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDetail {
    public List<String> getNudeDetail(String url) {
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            String pageNum_String = doc.getElementsByAttributeValue("class", "pagenavi").first().getElementsByTag("a").get(4).getElementsByTag("span")
                    .text();//获取总页数
            int pageNum = Integer.parseInt(pageNum_String);
            String firstpicurl = new String();
            Connection connection = Jsoup.connect(url).referrer(url).userAgent("Mozilla");
            Connection.Response response = connection.execute();
            System.out.println(response.statusCode());
            if (response.statusCode() == 200) {
                try {
                    org.jsoup.nodes.Document document = connection.get();
                    firstpicurl = document.getElementsByAttributeValue("class", "main-image").first().getElementsByTag("p").first()
                            .getElementsByTag("a").first().getElementsByTag("img").first().attr("src");
                    System.out.println(firstpicurl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<String> num = generateNum(pageNum);
            List<String> picUrlList = new ArrayList<>();
            for (String numm : num) {
                picUrlList.add(generateUrl(firstpicurl, numm));
            }

            return picUrlList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> generateNum(int maxNum) {
        if (maxNum < 100) {
            List<String> generatedNum = new ArrayList<>();
            for (int i = 1; i <= maxNum; i++) {
                if (i < 10) {
                    generatedNum.add("0" + i);
                } else {
                    generatedNum.add(i + "");
                }
            }
            return generatedNum;
        }
        if (maxNum < 1000 && maxNum > 100) {
            List<String> generatedNum = new ArrayList<>();
            for (int i = 1; i < maxNum; i++) {
                if (i < 10) {
                    generatedNum.add("00" + i);
                }
                if (i < 100 && i > 10) {
                    generatedNum.add("0" + i);
                }
                if (i >= 100) {
                    generatedNum.add(i + "");
                }
            }
            return generatedNum;
        }
        return null;
    }

    private String generateUrl(String firstUrl, String num) {
        String qian = firstUrl.substring(0, firstUrl.length() - 6);
        String hou = firstUrl.substring(firstUrl.length() - 4);
        return qian + num + hou;
    }
}

