package com.test.admin.conurbations.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.test.admin.conurbations.annotations.FindView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class BeautifulArticleItemDetailFragment extends BaseFragment {

    public static final String ITEM_URL = "url";
    public static final String ITEM_TITLE_ID = "item_title_id";
    public static final String ITEM_TITLE = "item_title";
    String detailContext;
    private static String text;
    @FindView
    TextView mContentTextView;
    @FindView
    CollapsingToolbarLayout mContentCollapsingToolbarLayout;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123456) {
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/jianshi_default.otf");
                mContentTextView.setTypeface(tf);
                mContentTextView.setText(detailContext);
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
    public BaseFragment newInstance() {
        return null;
    }

    @Override
    protected void initData(Bundle bundle) {
        if (getArguments().containsKey(ITEM_URL)) {
            show(getArguments().getString(ITEM_URL));
            String title = getArguments().getString(ITEM_TITLE);
            if (mContentCollapsingToolbarLayout != null) {
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/jianshi_default.otf");
                mContentCollapsingToolbarLayout.setCollapsedTitleTypeface(tf);
                mContentCollapsingToolbarLayout.setTitle(title);
            }
        }
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