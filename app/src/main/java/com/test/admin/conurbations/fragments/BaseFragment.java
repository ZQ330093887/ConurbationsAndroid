package com.test.admin.conurbations.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.activitys.IBaseView;
import com.test.admin.conurbations.annotations.SetLayout;
import com.test.admin.conurbations.utils.InjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouqiong on 2016/9/23.
 */
@SetLayout
public abstract class BaseFragment extends Fragment implements IBaseView {
    public abstract BaseFragment newInstance();

    private static final String TAG = "BaseFragment";

    protected View rootView;

    private LayoutInflater inflater;

    public Map<String, View> views;
    protected void initToolbar(Toolbar toolbar, String toolbarTitle, String toolbarSubtitle) {

        if (!TextUtils.isEmpty(toolbarTitle)){
            toolbar.setTitle(toolbarTitle);
        }
        if (!TextUtils.isEmpty(toolbarSubtitle)){
            toolbar.setSubtitle(toolbarSubtitle);
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    public void setRootView(int layoutId) {
        rootView = inflater.inflate(layoutId, null);
    }

    public View getRootView() {
        return rootView;
    }

    protected abstract void initData(Bundle bundle);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        views = new HashMap<>();
        InjectUtil.inject(this);
        initData(savedInstanceState);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void startActivity(Class<?> cls) {
        getBaseActivity().startActivity(cls);
    }

    @Override
    public void startActivityAndFinishWithoutTransition(Class<?> cls) {
        getBaseActivity().startActivityAndFinishWithoutTransition(cls);
    }

    @Override
    public void startActivity(Class<?> cls, Bundle bundle) {
        getBaseActivity().startActivity(cls, bundle);
    }

    @Override
    public void startActivityAndClear(Class<?> cls) {
        getBaseActivity().startActivityAndClear(cls);
    }

    @Override
    public void startActivityAndFinish(Class<?> cls) {
        getBaseActivity().startActivityAndFinish(cls);
    }

    @Override
    public void startActivityAndFinish(Class<?> cls, Bundle bundle) {
        getBaseActivity().startActivityAndFinish(cls, bundle);
    }

    @Override
    public void startActivityAndFinishWithoutTransition(Class<?> cls, Bundle bundle) {
        getBaseActivity().startActivityAndFinishWithoutTransition(cls, bundle);
    }

    @Override
    public void startActivityForResultWithoutTransition(Class<?> cls) {
        getBaseActivity().startActivityForResultWithoutTransition(cls);
    }

    @Override
    public void startActivityForResultWithoutTransition(Class<?> cls, int requestCode) {
        getBaseActivity().startActivityForResultWithoutTransition(cls, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> cls) {
        getBaseActivity().startActivityForResult(cls);
    }

    @Override
    public void startActivityForResult(Class<?> cls, Bundle bundle) {
        getBaseActivity().startActivityForResult(cls, bundle);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode) {
        getBaseActivity().startActivityForResult(cls, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        getBaseActivity().startActivityForResult(cls, requestCode, bundle);
    }
    @Override
    public void finishActivity() {
        getBaseActivity().finishActivity();
    }

    @Override
    public Activity getRootActivity() {
        return getBaseActivity().getRootActivity();
    }

    @Override
    public Context getApplicationContext() {
        return getBaseActivity().getApplicationContext();
    }

    @Override
    public void startActivityAndFinishWithOutObservable(Class<?> cls) {

    }
}
