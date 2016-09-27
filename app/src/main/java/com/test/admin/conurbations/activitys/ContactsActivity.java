package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentContactsAdapter;
import com.test.admin.conurbations.config.Contact;
import com.test.admin.conurbations.views.WaveSideBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2015/9/23.
 */
public class ContactsActivity extends AppCompatActivity {

    @Bind(R.id.rv_contacts)
    RecyclerView rvContacts;
    @Bind(R.id.side_bar)
    WaveSideBar sideBar;

    private ArrayList<Contact> contacts = new ArrayList<>();
    private FragmentContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

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

    private void initData() {
        contacts.addAll(Contact.getNumber(this));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
