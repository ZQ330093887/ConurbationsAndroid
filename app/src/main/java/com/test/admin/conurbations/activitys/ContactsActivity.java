package com.test.admin.conurbations.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.adapter.FragmentContactsAdapter;
import com.test.admin.conurbations.config.Contact;
import com.test.admin.conurbations.databinding.ActivityContactsBinding;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.views.WaveSideBar;

import java.util.ArrayList;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public class ContactsActivity extends BaseActivity<ActivityContactsBinding> {

    private ArrayList<Contact> mContactsList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initData(Bundle bundle) {
        initView();
        mContactsList.addAll(Contact.getNumber(this));
    }

    private void initView() {
        FragmentContactsAdapter mContactsAdapter = new FragmentContactsAdapter(mContactsList, R.layout.item_contacts);

        mBinding.rvContactsView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContactsView.setAdapter(mContactsAdapter);
        mContactsAdapter.setOnItemClickListener((view, data) ->
                ToastUtils.getInstance().showToast(data.getNumber()));
        mBinding.wsbContactsView.setPosition(WaveSideBar.POSITION_RIGHT);
        mBinding.wsbContactsView.setOnSelectIndexItemListener(index -> {
            for (int i = 0; i < mContactsList.size(); i++) {
                if (mContactsList.get(i).getIndex().equals(index)) {
                    ((LinearLayoutManager) mBinding.rvContactsView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });
    }
}
