package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.WonderfulFictionAdapter;
import com.test.admin.conurbations.model.BooksBean;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.widget.MyGridLayoutManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class WonderfulFictionActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.wonderful_fiction_rv)
    RecyclerView recyclerView;
    public static List<String[]> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    private String[] urls, title;
    private WonderfulFictionAdapter fictionAdapter;
    private List<BooksBean> booksBeen = new ArrayList<>();
    private List<BooksBean> leftBooksBeen = new ArrayList<>();
    private List<BooksBean> rankContentBooksBeen = new ArrayList<>();
    private List<BooksBean> rankTitleBooksBeen = new ArrayList<>();
    private List<BooksBean> updateBooksBeen = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                DialogUtils.hideProgressDialog();
                fictionAdapter.getBannerData(images, titles, leftBooksBeen, rankTitleBooksBeen, rankContentBooksBeen,updateBooksBeen);
                fictionAdapter.setList(booksBeen);
                fictionAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_wonderful_fiction;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(toolbar, "热门小说", "");
        DialogUtils.showProgressDialog(this);
        show();
        fictionAdapter = new WonderfulFictionAdapter();
        recyclerView.setLayoutManager(new MyGridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fictionAdapter);
    }

    @Override
    protected void initPresenter() {
    }

    private void show() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Document doc = Jsoup.connect("https://www.readnovel.com/").get();
                        /**获取banner数据开始*/
                        Elements divs = doc.select(".focus-slider-wrap");
                        Document divContions = Jsoup.parse(divs.toString());
                        Elements element = divContions.getElementsByTag("li");
                        urls = new String[element.size()];
                        title = new String[element.size()];
                        for (int i = 0; i < element.size(); i++) {
                            Element links = element.get(i);
                            String imgUrl = links.getElementsByTag("img").attr("src");
                            String titleName = links.getElementsByTag("img").attr("alt");
                            urls[i] = imgUrl;
                            title[i] = titleName;
                        }
                        List list = Arrays.asList(urls);
                        images = new ArrayList(list);

                        List list1 = Arrays.asList(title);
                        titles = new ArrayList(list1);
                        /**获取banner数据结束*/
                        /**获取新书抢鲜数据开始*/
                        /**获取  --新书抢鲜--  数据开始*/
                        Elements rightBookeElements = doc.select(".right-book-list");
                        Document rightDocument = Jsoup.parse(rightBookeElements.toString());
                        Elements rightElements = rightDocument.getElementsByTag("li");

                        for (Element rigElement : rightElements) {
                            BooksBean booksBean = new BooksBean();
                            String imgUrl = rigElement.getElementsByTag("img").attr("data-original");
                            String titleName = rigElement.getElementsByTag("img").attr("alt");
                            String author = rigElement.getElementsByTag("h4").text();
                            String type = rigElement.getElementsByTag("span").get(0).text();
                            String script = rigElement.getElementsByTag("span").get(1).text();
                            String scriptNumber = rigElement.getElementsByTag("span").get(2).text();
                            String textContent = rigElement.getElementsByTag("p").get(1).text();
                            booksBean.setImgUrl(imgUrl);
                            booksBean.setTitle(titleName);
                            booksBean.setAuthor(author);
                            booksBean.setType(type);
                            booksBean.setScript(script);
                            booksBean.setScriptNumber(scriptNumber);
                            booksBean.setTextContent(textContent);
                            booksBeen.add(booksBean);
                        }
                        /**获取  --新书抢鲜--  数据结束*/

                        /**获取  --热门小说--  数据开始*/
                        Elements leftBookElements = doc.select(".book-list-row");
                        Document leftDocument = Jsoup.parse(leftBookElements.toString());
                        Elements leftElements = leftDocument.getElementsByTag("li");
                        for (int i = 0; i < 8; i++) {
                            Element leftElement = leftElements.get(i);
                            BooksBean booksBean = new BooksBean();
                            String imgUrl = leftElement.getElementsByTag("img").attr("src");
                            String textContent = leftElement.getElementsByTag("img").attr("alt");
                            booksBean.setImgUrl(imgUrl);
                            booksBean.setTextContent(textContent);
                            leftBooksBeen.add(booksBean);
                        }
                        /**获取  --热门小说--  数据结束*/
                        /**-- start 书单排行榜 --*/
                        Elements rankBooksElements = doc.select(".rank-list");
                        Document rankDocument = Jsoup.parse(rankBooksElements.toString());
                        Elements rankElementLi = rankDocument.getElementsByTag("li");
                        Elements rankElementH3 = rankDocument.getElementsByTag("h3");
                        for (Element rankElement : rankElementH3) {
                            BooksBean booksBean = new BooksBean();
                            String title = rankElement.text();
                            booksBean.setTitle(title);
                            rankTitleBooksBeen.add(booksBean);
                        }

                        for (Element rankElement : rankElementLi) {
                            BooksBean booksBean = new BooksBean();
                            String textContent = rankElement.text();
                            booksBean.setTextContent(textContent);
                            rankContentBooksBeen.add(booksBean);
                        }

                        /**-- end 书单排行榜 --*/

                        /**-- start 最近更新 --*/
                        Elements recentUpdatesElements = doc.select(".update-wrap");
                        Document recentUpdatesDocument = Jsoup.parse(recentUpdatesElements.toString());
                        Elements upDateElements = recentUpdatesDocument.getElementsByTag("tr");
                        for (Element rankElement : upDateElements) {
                            BooksBean booksBean = new BooksBean();
                            booksBean.setType(rankElement.getElementsByClass("type").text());
                            booksBean.setTitle(rankElement.getElementsByClass("name").text());
                            booksBean.setTextContent(rankElement.getElementsByClass("chapter").text());
                            booksBean.setAuthor(rankElement.getElementsByClass("writer").text());
                            booksBean.setScript(rankElement.getElementsByClass("words").text());
                            booksBean.setScriptNumber(rankElement.getElementsByClass("time").text());
                            updateBooksBeen.add(booksBean);
                        }
                        /**-- end 最近更新 --*/

                        Looper.prepare();
                        Message msg = Message.obtain();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                        Looper.loop();
                    } catch (Exception e) {
                        Log.i("mytag", e.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
