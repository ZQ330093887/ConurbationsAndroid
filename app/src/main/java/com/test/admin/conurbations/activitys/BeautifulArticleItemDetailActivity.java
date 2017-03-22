package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Toast;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.BeautifulArticleItemDetailFragment;

import butterknife.Bind;

public class BeautifulArticleItemDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_beautiful_article_item_detail_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab_beautiful_article_item_detail_fab)
    FloatingActionButton mFloatingActionButton;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_beautiful_article_item_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mToolbar, "", "");
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(BeautifulArticleItemDetailFragment.ITEM_URL, getIntent().getStringExtra(BeautifulArticleItemDetailFragment.ITEM_URL));
            arguments.putString(BeautifulArticleItemDetailFragment.ITEM_TITLE, getIntent().getStringExtra(BeautifulArticleItemDetailFragment.ITEM_TITLE));
            BeautifulArticleItemDetailFragment fragment = new BeautifulArticleItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ns_beautiful_article_item_detail_container, fragment)
                    .commit();
        }

        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    protected void initPresenter() {
    }

    public void CopyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text); // 复制
        Toast.makeText(context, "文本已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        CopyToClipboard(v.getContext(), BeautifulArticleItemDetailFragment.getItemContext());
    }
}
