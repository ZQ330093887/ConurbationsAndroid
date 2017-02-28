package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Toast;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.fragments.ItemDetailFragment;

import butterknife.Bind;

public class ItemDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;
    @Bind(R.id.item_detail_fab)
    FloatingActionButton fab;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_item_detail;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(toolbar, "", "");
        if (bundle == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ITEM_URL, getIntent().getStringExtra(ItemDetailFragment.ITEM_URL));
            arguments.putString(ItemDetailFragment.ITEM_TITLE, getIntent().getStringExtra(ItemDetailFragment.ITEM_TITLE));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }

        fab.setOnClickListener(this);
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
        CopyToClipboard(v.getContext(), ItemDetailFragment.getItemContext());
    }
}
