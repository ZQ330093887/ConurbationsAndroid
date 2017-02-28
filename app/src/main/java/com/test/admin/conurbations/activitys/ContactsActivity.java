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

    @Bind(R.id.rv_contacts)
    RecyclerView rvContacts;
    @Bind(R.id.side_bar)
    WaveSideBar sideBar;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private FragmentContactsAdapter contactsAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.contacts_activity;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
        contacts.addAll(Contact.getNumber(this));
    }

    @Override
    protected void initPresenter() {}

    private void initView() {
        contactsAdapter = new FragmentContactsAdapter(contacts, R.layout.item_contacts);

        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(contactsAdapter);
        contactsAdapter.setOnItemClickListener(new FragmentContactsAdapter.OnRecyclerViewItemClickListener(){
            @Override
            public void onItemClick(View view , Contact data){
                Toast.makeText(ContactsActivity.this, data.getNumber(), Toast.LENGTH_SHORT).show();
            }
        });
        sideBar.setPosition(WaveSideBar.POSITION_RIGHT);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i=0; i<contacts.size(); i++) {
                    if (contacts.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) rvContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }
}
