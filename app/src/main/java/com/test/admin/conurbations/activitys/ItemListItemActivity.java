package com.test.admin.conurbations.activitys;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.BookBean;

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
    List<BookBean> list = new ArrayList<>();
    SimpleItemRecyclerViewAdapter viewAdapter;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity_item_list);
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

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        viewAdapter = new SimpleItemRecyclerViewAdapter(list);
        recyclerView.setAdapter(viewAdapter);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
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
                            BookBean bookBean = new BookBean();
                            String title = links.getElementsByTag("a").get(0).text();
                            String url = links.select("a").attr("href");
                            bookBean.setTitle(title);
                            bookBean.setUrl(url);
                            list.add(bookBean);
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

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<BookBean> mValues;

        public SimpleItemRecyclerViewAdapter(List<BookBean> list) {
            mValues = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mContentView.setText((position + 1) + ".  " + mValues.get(position).getTitle());
            holder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ITEM_URL, mValues.get(position).getUrl());
                        arguments.putString(ItemDetailFragment.ITEM_TITLE, mValues.get(position).getTitle());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ITEM_URL, mValues.get(position).getUrl());
                        intent.putExtra(ItemDetailFragment.ITEM_TITLE, mValues.get(position).getTitle());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues != null ? mValues.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mContentView;

            public ViewHolder(View view) {
                super(view);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

}