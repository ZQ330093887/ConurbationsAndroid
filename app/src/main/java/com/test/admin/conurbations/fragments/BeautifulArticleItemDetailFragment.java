package com.test.admin.conurbations.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.admin.conurbations.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class BeautifulArticleItemDetailFragment extends Fragment {

    public static final String ITEM_URL = "url";
    public static final String ITEM_TITLE_ID = "item_title_id";
    public static final String ITEM_TITLE = "item_title";
    String detailContext;
    private static String text;
    private TextView textContext;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123456) {
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/jianshi_default.otf");
                textContext.setTypeface(tf);
                textContext.setText(detailContext);
                text = detailContext;
            }
        }
    };

   public static String getItemContext(){
       if (!TextUtils.isEmpty(text)){
           return text;
       }
       return "暂无数据可复制";
   }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ITEM_URL)) {
            show(getArguments().getString(ITEM_URL));
            String title = getArguments().getString(ITEM_TITLE);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.ctl_beautiful_article_item_detail_head);
            if (appBarLayout != null) {
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/jianshi_default.otf");
                appBarLayout.setCollapsedTitleTypeface(tf);
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_beautiful_article_detail, container, false);
        textContext = ((TextView) rootView.findViewById(R.id.tv_beautiful_article_detail));
        return rootView;
    }

    private void show(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Document doc = Jsoup.connect(url).get();
                        StringBuilder sbContent = new StringBuilder();
                        Elements elements = doc.select("div.PostContent > p");
                        for (int i = 0; i < elements.size(); i++) {
                            Element ele = elements.get(i);
                            sbContent.append(ele.ownText() + "\n\n");
                        }
                        detailContext = sbContent.toString();

                        Looper.prepare();
                        Message msg = Message.obtain();
                        msg.what = 0x123456;
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