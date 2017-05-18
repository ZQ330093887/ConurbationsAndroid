package com.test.admin.conurbations.utils;

import android.util.Log;

import com.test.admin.conurbations.model.entity.VideoLiveInfo;
import com.test.admin.conurbations.model.entity.VideoLiveSource;
import com.test.admin.conurbations.model.response.VideoLiveData;
import com.test.admin.conurbations.model.response.VideoLiveSourceData;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author yuyh.
 * @date 2016/12/23.
 */
public class TmiaaoUtils {


    static class LiveContentHandler extends DefaultHandler {

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            Log.d("SprintNBA", "startElement localName " + localName);

            for (int i = 0; i < attributes.getLength(); i++) {

                String[] str = new String[2];

                Log.d("NBAXXXX", "attributes1:" + attributes.getLocalName(i));

                Log.d("SprintNBA", "attributes2:" + attributes.getValue(i));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String str = new String(ch, start, length);

            Log.d("SprintNBA", "characters: " + str);

            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            Log.d("SprintNBA", "endElement " + localName);
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }
    }

    void parse() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        SAXParser parser = factory.newSAXParser();

        XMLReader reader = parser.getXMLReader();

        reader.setContentHandler(new LiveContentHandler());

        reader.parse(new InputSource(new ByteArrayInputStream(null)));
    }


    public static VideoLiveData getLiveList() {

        try {
            String html = getHtml("http://nba.tmiaoo.com/ipad.html");

            int start = html.indexOf("<script src=");

            if (start > 0) {

                int end = html.indexOf("</script>", start);

                String script = html.substring(start + 1, end)
                        .replaceAll("\n", "");


                String jsPath = script.substring(script.indexOf("\"") + 1, script.lastIndexOf("\""));


                String jsStr = getHtml("http://nba.tmiaoo.com" + jsPath);


                String bodyStr = jsStr.substring(jsStr.indexOf("\"") + 1, jsStr.lastIndexOf("\""))
                        .replaceAll("\\\\", "");

                return parseBody(bodyStr);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static VideoLiveSourceData getSourceList(String link) {
        String html = "";
        VideoLiveSourceData list = new VideoLiveSourceData();

        try {
            html = getHtml(link);
            // NBA 等正常直播源
            if (html.replaceAll("\n", "").startsWith("<script") && html.replaceAll("\n", "").endsWith("</script>")) {
                String[] p = link.split("[?]");
                if (p.length > 0) {
                    String url = p[0];
                    if (!url.endsWith("/")) {
                        url += "/";
                    }
                    url += "w.html";
                    html = getHtml(url);
                } else {
                    return null;
                }
            } else if (html.contains("mv_action")) {
                // continue
            } else if (link.contains("cctv5") || html.replaceAll("\n", "").startsWith("<script")) {
                VideoLiveSource source = new VideoLiveSource();
                source.link = link;
                source.name = "单一直播源";
                list.items.add(source);
                return list;
            } else {
                VideoLiveSource source = new VideoLiveSource();
                source.link = link;
                source.name = "单一直播源";
                list.items.add(source);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int start = 0, end = 0;
        int hrefStart = 0, hrefEnd = 0;
        String href;

        while ((start = html.indexOf("<div class=\"mv_action\">", start)) >= 0) {

            VideoLiveSource source = new VideoLiveSource();

            hrefStart = html.indexOf("href=\"", start) + 6;
            hrefEnd = html.indexOf("\"", hrefStart);
            href = html.substring(hrefStart, hrefEnd);

            source.link = href;

            hrefStart = html.indexOf("\">", hrefEnd) + 2;
            hrefEnd = html.indexOf("</a>", hrefStart);
            href = html.substring(hrefStart, hrefEnd);

            source.name = href;

            list.items.add(source);

            start = hrefEnd + 2;
        }

        return list;
    }

    private static VideoLiveData parseBody(String bodyStr) {

        int start = 0, end = 0;
        int hrefStart = 0, hrefEnd = 0;
        String href;


        VideoLiveData videoLiveData = new VideoLiveData();

        while ((start = bodyStr.indexOf("<div class=\"team-content\">", start + 1)) >= 0) {


            try { // 牺牲一定的性能来提高容错性

                VideoLiveInfo info = new VideoLiveInfo();

                start = bodyStr.indexOf("<a data-action", start);

                hrefStart = bodyStr.indexOf("href=\"", start) + 6;
                hrefEnd = bodyStr.indexOf("\"", hrefStart);

                href = bodyStr.substring(hrefStart, hrefEnd);
                info.link = href;

                start = bodyStr.indexOf("<div class=\"team-left\">", hrefEnd);

                hrefStart = bodyStr.indexOf("<img src=", start) + 10;
                hrefEnd = bodyStr.indexOf("\">", hrefStart);
                href = bodyStr.substring(hrefStart, hrefEnd);
                info.leftImg = href;

                hrefStart = bodyStr.indexOf("<span>", start) + 6;
                hrefEnd = bodyStr.indexOf("</span>", hrefStart);
                href = bodyStr.substring(hrefStart, hrefEnd);
                info.leftName = href;

                // time
                hrefStart = bodyStr.indexOf("<p class=\"score-content\">", hrefEnd);
                hrefStart = bodyStr.indexOf("<span>", hrefStart) + 6;
                hrefEnd = bodyStr.indexOf("</span>", hrefStart);
                href = bodyStr.substring(hrefStart, hrefEnd);
                info.time = href;

                // right team
                start = bodyStr.indexOf("<div class=\"team-right\"", hrefEnd);

                hrefStart = bodyStr.indexOf("<img src=", start) + 10;
                hrefEnd = bodyStr.indexOf("\">", hrefStart);
                href = bodyStr.substring(hrefStart, hrefEnd);
                info.rightImg = href;

                hrefStart = bodyStr.indexOf("<span>", start) + 6;
                hrefEnd = bodyStr.indexOf("</span>", hrefStart);
                href = bodyStr.substring(hrefStart, hrefEnd);
                info.rightName = href;
                videoLiveData.items.add(info);
            } catch (Exception e) {
                Log.e("Error!!!", e.toString());
            } finally {
                start = hrefEnd + 2;
            }
        }

        return videoLiveData;
    }

    public static String getHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        InputStream inStream = conn.getInputStream();
        byte[] data = readInputStream(inStream);
        String html = new String(data, "utf-8");
        return html;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
