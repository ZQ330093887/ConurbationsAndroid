package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentContactsAdapter;
import com.test.admin.conurbations.annotations.FindView;
import com.test.admin.conurbations.config.Contact;
import com.test.admin.conurbations.views.WaveSideBar;

import java.util.ArrayList;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class ContactsActivity extends BaseActivity {

    @FindView
    RecyclerView mViewRecyclerView;
    @FindView
    WaveSideBar mViewWaveSideBar;
    private ArrayList<Contact> mContactsList = new ArrayList<>();
    private FragmentContactsAdapter mContactsAdapter;

    @Override
    protected void initData(Bundle bundle) {
        initView();
        mContactsList.addAll(Contact.getNumber(this));
    }

    @Override
    protected void initPresenter() {}

    private void initView() {
        mContactsAdapter = new FragmentContactsAdapter(mContactsList, R.layout.item_contacts);

        mViewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mViewRecyclerView.setAdapter(mContactsAdapter);
        mContactsAdapter.setOnItemClickListener((view, data) -> Toast.makeText(ContactsActivity.this, data.getNumber(), Toast.LENGTH_SHORT).show());
        mViewWaveSideBar.setPosition(WaveSideBar.POSITION_RIGHT);
        mViewWaveSideBar.setOnSelectIndexItemListener(index -> {
            for (int i = 0; i< mContactsList.size(); i++) {
                if (mContactsList.get(i).getIndex().equals(index)) {
                    ((LinearLayoutManager) mViewRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });
    }
}
