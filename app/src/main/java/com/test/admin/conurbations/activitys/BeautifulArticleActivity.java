package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.test.admin.conurbations.adapter.BeautifulArticleAdapter;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.model.entity.BooksBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class BeautifulArticleActivity extends BaseActivity {

    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    RecyclerView mContentRecyclerView;
    List<BooksBean> mBeautifulArticleList = new ArrayList<>();
    BeautifulArticleAdapter mArticleAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                mArticleAdapter.setList(mBeautifulArticleList);
                mArticleAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, "", "");
        show();
        mArticleAdapter = new BeautifulArticleAdapter();
        mContentRecyclerView.setAdapter(mArticleAdapter);
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
                        Document doc = Jsoup.connect("http://www.lz13.cn/lizhi/lizhiwenzhang.html").get();
                        Elements divs = doc.select("#MainMenu");
                        Document divContions = Jsoup.parse(divs.toString());
                        Elements element = divContions.getElementsByTag("li");
                        for (Element links : element) {
                            BooksBean booksBean = new BooksBean();
                            String title = links.getElementsByTag("a").text();
                            String url = links.select("a").attr("href");
                            booksBean.setTitle(title);
                            booksBean.setUrl(url);
                            mBeautifulArticleList.add(booksBean);
                        }
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