package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.SimpleItemListRecyclerViewAdapter;
import com.test.admin.conurbations.fragments.ItemDetailFragment;
import com.test.admin.conurbations.model.BooksBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemListItemActivity extends AppCompatActivity {
    @Bind(R.id.item_toolbar)
    Toolbar toolbar;
    @Bind(R.id.item_list)
    RecyclerView recyclerView;
    List<BooksBean> list = new ArrayList<>();
    SimpleItemListRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity_item_lists);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra(ItemDetailFragment.ITEM_TITLE_ID);
        if (!TextUtils.isEmpty(url)) {
            show(url);
        }

        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra("item_title"));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewAdapter = new SimpleItemListRecyclerViewAdapter();
        recyclerView.setAdapter(viewAdapter);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                viewAdapter.setList(list);
                viewAdapter.notifyDataSetChanged();
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
                            list.add(booksBean);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}