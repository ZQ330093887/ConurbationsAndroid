package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.test.admin.conurbations.adapter.BeautifulArticleItemAdapter;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.fragments.BeautifulArticleItemDetailFragment;
import com.test.admin.conurbations.model.entity.BooksBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ItemBeautifulArticleActivity extends BaseActivity {
    @FindView
    Toolbar mToolbarToolbar;
    @FindView
    RecyclerView mContentRecyclerView;
    List<BooksBean> mBeautifulArticleItemList = new ArrayList<>();
    BeautifulArticleItemAdapter mArticleItemAdapter;

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbarToolbar, getIntent().getStringExtra("item_title"), "");

        String url = getIntent().getStringExtra(BeautifulArticleItemDetailFragment.ITEM_TITLE_ID);
        if (!TextUtils.isEmpty(url)) {
            show(url);
        }

        mArticleItemAdapter = new BeautifulArticleItemAdapter();
        mContentRecyclerView.setAdapter(mArticleItemAdapter);
    }

    @Override
    protected void initPresenter() {
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mArticleItemAdapter.setList(mBeautifulArticleItemList);
                mArticleItemAdapter.notifyDataSetChanged();
            }
        }
    };

    private void show(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Document doc = Jsoup.connect(url).get();
                        Elements divs = doc.select("div.PostHead");
                        Document divcontions = Jsoup.parse(divs.toString());
                        Elements element = divcontions.getElementsByTag("h3");

                        for (Element links : element) {
                            BooksBean booksBean = new BooksBean();
                            String title = links.getElementsByTag("a").get(0).text();
                            String url = links.select("a").attr("href");
                            booksBean.setTitle(title);
                            booksBean.setUrl(url);
                            mBeautifulArticleItemList.add(booksBean);
                        }
                        Looper.prepare();
                        Message msg = Message.obtain();
                        msg.what = 1;
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