package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentContactsAdapter;
import com.test.admin.conurbations.config.Contact;
import com.test.admin.conurbations.views.WaveSideBar;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class ContactsActivity extends BaseActivity {

    @Bind(R.id.rv_contacts_recycle_view)
    RecyclerView mViewRecyclerView;
    @Bind(R.id.wsb_contacts_wave)
    WaveSideBar mWaveSideBar;
    private ArrayList<Contact> mContactsList = new ArrayList<>();
    private FragmentContactsAdapter mContactsAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.contacts_activity;
    }

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
        mContactsAdapter.setOnItemClickListener(new FragmentContactsAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view , Contact data){
                Toast.makeText(ContactsActivity.this, data.getNumber(), Toast.LENGTH_SHORT).show();
            }
        });
        mWaveSideBar.setPosition(WaveSideBar.POSITION_RIGHT);
        mWaveSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i< mContactsList.size(); i++) {
                    if (mContactsList.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) mViewRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }
}
