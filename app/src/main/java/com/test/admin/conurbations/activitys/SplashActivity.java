package com.test.admin.conurbations.activitys;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.Snackbar;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivitySplashBinding;
import com.test.admin.conurbations.utils.AppUtils;
import com.test.admin.conurbations.utils.PrefUtils;
import com.test.admin.conurbations.views.AlertDialog;

import java.util.Random;

import io.reactivex.disposables.Disposable;


/**
 * Created by ZQiong on 2017/1/21.
 */

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    public AMapLocationClient mLocationClient = null;
    private Disposable subscribe;

    private static final int REQUEST_PERMISSION_SEETING = 1000;
    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;
    private static final int[] SPLASH_ARRAY = {
            R.mipmap.splash0,
            R.mipmap.splash1,
            R.mipmap.splash2,
            R.mipmap.splash3,
            R.mipmap.splash4,

            R.mipmap.splash6
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData(Bundle bundle) {

        Random r = new Random(SystemClock.elapsedRealtime());
        mBinding.ivSplashLogo.setImageResource(SPLASH_ARRAY[r.nextInt(SPLASH_ARRAY.length)]);
        mBinding.tvAboutVersion.setText(AppUtils.getVersion(this));

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
        mLocationClient.setLocationOption(mLocationOption);
        applyPermissions();

    }

    private void applyPermissions() {
        subscribe = new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        animateImage();
                    } else {
                        startToSetting();
                        Snackbar.make(mBinding.welContainer, getResources().getString(R.string.permission_hint),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(getResources().getString(R.string.sure), view -> applyPermissions()).show();
                    }
                });
    }

    private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mBinding.ivSplashLogo, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mBinding.ivSplashLogo, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startActivityAndFinishWithOutObservable(MainActivity.class);
            }
        });
    }

    private void startToSetting() {
        AlertDialog alertDialog = new AlertDialog(this).builder();
        alertDialog.setTitle("去设置界面开启权限?");
        alertDialog.setMsg("权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用");
        alertDialog.setLeft();
        alertDialog.setNegativeButton("取消", v -> {
            finish();
        });
        alertDialog.setPositiveButton("开启权限", v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SEETING);
        });
        alertDialog.show();
    }

    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = amapLocation -> {
        String city = "北京";
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                city = amapLocation.getCity();
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            }

            PrefUtils.putString(this, city, city);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是从设置界面返回,就继续判断权限
        if (requestCode == REQUEST_PERMISSION_SEETING) {
            applyPermissions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
            subscribe = null;
        }
    }

    @Override
    public void detachView() {
    }
}
