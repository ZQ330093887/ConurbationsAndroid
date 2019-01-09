package com.test.admin.conurbations.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.test.admin.conurbations.IMusicService;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.SetLayout;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.WrapperUtils;
import com.test.admin.conurbations.utils.bigImageView.glide.engine.SimpleFileTarget;
import com.yalantis.contextmenu.lib.MenuObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhouqiong on 2017/2/27.
 */

public abstract class BaseActivity<VB extends ViewDataBinding> extends
        AppCompatActivity implements ServiceConnection, IBaseView {

    private static final String TAG = "BaseActivity";
    protected VB mBinding;
    public static final String TRANSLATE_VIEW = "translate_view";
    public static final String TRANSLATE_WEB_VIEW_BG_IMG = "translate_web_view_bg_img";
    public static final String TRANSLATE_WEB_VIEW_TITLE = "translate_web_view_title";

    public static final String EXTRA_URL = "URL";
    public static final String EXTRA_TITLE = "TITLE";
    public static final String BUNDLE_KEY_SHOW_BOTTOM_BAR = "BUNDLE_KEY_SHOW_BOTTOM_BAR";
    public static final String BUNDLE_OVERRIDE = "BUNDLE_OVERRIDE";
    private PlayManager.ServiceToken mToken;

    protected abstract int getLayoutId();

    protected abstract void initData(Bundle bundle);

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    protected void initToolbar(Toolbar toolbar, String toolbarTitle, String toolbarSubtitle) {

        if (!TextUtils.isEmpty(toolbarTitle)) {
            toolbar.setTitle(toolbarTitle);
        }
        if (!TextUtils.isEmpty(toolbarSubtitle)) {
            toolbar.setSubtitle(toolbarSubtitle);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = PlayManager.bindToService(this, this);
        if (!isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finishActivity();
                return;
            }
        }
        this.mBinding = DataBindingUtil.setContentView(this, this.getLayoutId());
        initData(savedInstanceState);
    }

    @Override
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
        if (mToken != null) {
            PlayManager.unbindFromService(mToken);
            mToken = null;
        }
    }

    @Override
    public void startActivityAndClear(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
        CommonUtil.exit();
    }

    @Override
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startActivityAndFinish(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finishAfterTransition();
        } else {
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void startActivityAndFinishWithoutTransition(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityAndFinish(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finishAfterTransition();
        } else {
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void startActivityAndFinishWithOutObservable(final Class<?> cls) {
        final Intent intent = new Intent();
        intent.setClass(this, cls);
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    startActivity(intent);
                    overridePendingTransition(0, android.R.anim.fade_out);
                    finish();
                });
    }

    @Override
    public void startActivityAndFinishWithoutTransition(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityForResultWithoutTransition(Class<?> cls) {
        startActivityForResultWithoutTransition(cls, 0);
    }

    @Override
    public void startActivityForResultWithoutTransition(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> cls) {
        startActivityForResult(cls, 0);
    }

    @Override
    public void startActivityForResult(Class<?> cls, Bundle bundle) {
        startActivityForResult(cls, 0, bundle);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public Activity getRootActivity() {
        Activity activity = this;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        return activity;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.finishAfterTransition();
        } else {
            this.finish();
        }
    }

    public static List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.more_ic_close);

        MenuObject addFr = new MenuObject("下载图片");
        addFr.setResource(R.mipmap.more_ic_download);
        MenuObject addShare = new MenuObject("分享图片");
        addShare.setResource(R.mipmap.more_ic_share);
        MenuObject addSave = new MenuObject("收藏图片");
        addSave.setResource(R.mipmap.more_ic_collection);
        MenuObject block = new MenuObject("设为桌面背景");
        block.setResource(R.mipmap.more_ic_backage);
        MenuObject block2 = new MenuObject("设为锁屏背景");
        block2.setResource(R.mipmap.more_ic_loakclose);

        menuObjects.add(close);
        menuObjects.add(addFr);
        menuObjects.add(addShare);
        menuObjects.add(addSave);
        menuObjects.add(block);
        menuObjects.add(block2);
        return menuObjects;
    }

    public static void downloadBitmap(final Context context, String url, final int state, final String path, final String name) {
        Glide.with(context).load(url).downloadOnly(new SimpleFileTarget() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                if (state == 1) {
                    //保存图片
                    SaveBitmapUtils.getSaveBitmapObservable(resource, path, name).subscribe(SaveBitmapUtils.getSaveSubscriber(context, path));
                } else if (state == 2) {
                    //分享图片
                    SaveBitmapUtils.getSaveBitmapObservable(resource, path, name).subscribe(SaveBitmapUtils.getShareSubscriber(context, path));
                } else if (state == 4) {
                    //设置桌面壁纸
                    WrapperUtils.getSetWallWrapperObservable(resource, context).subscribe(WrapperUtils.callbackSubscriber);
                } else if (state == 5) {
                    //设置锁屏壁纸
                    WrapperUtils.getSetLockWrapperObservable(resource, context).subscribe(WrapperUtils.callbackSubscriber);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                ToastUtils.getInstance().showToast("操作失败！");
                DialogUtils.hideProgressDialog();
            }
        });


    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        PlayManager.mService = IMusicService.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        PlayManager.mService = null;
    }


    @Override
    public void detachView() {

    }
}
