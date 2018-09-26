package com.test.admin.conurbations.fragments;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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
import com.test.admin.conurbations.utils.AutoClearedValue;

/**
 * Created by zhouqiong on 2016/9/23.
 */
public abstract class BaseFragment<VB extends ViewDataBinding> extends Fragment implements IBaseView {

    private static final String TAG = "BaseFragment";
    protected AutoClearedValue<VB> mBinding;

    protected void initToolbar(Toolbar toolbar, String toolbarTitle, String toolbarSubtitle) {

        if (!TextUtils.isEmpty(toolbarTitle)) {
            toolbar.setTitle(toolbarTitle);
        }
        if (!TextUtils.isEmpty(toolbarSubtitle)) {
            toolbar.setSubtitle(toolbarSubtitle);
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> getActivity().finish());
    }

    protected abstract int getLayoutId();

    protected abstract void initData(Bundle bundle);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        VB dataBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
        this.mBinding = new AutoClearedValue(this, dataBinding);
        initData(savedInstanceState);

        return dataBinding.getRoot();
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
        detachView();
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
