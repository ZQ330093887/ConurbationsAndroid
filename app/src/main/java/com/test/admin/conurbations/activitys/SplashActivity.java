package com.test.admin.conurbations.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;

import com.test.admin.conurbations.R;

import java.util.Random;

import butterknife.Bind;


/**
 * Created by Eric on 2017/1/21.
 */

public class SplashActivity extends BaseActivity {

    private static final int ANIMATION_DURATION = 2000;
    private static final float SCALE_END = 1.13F;
    private static final int[] SPLASH_ARRAY = {
            R.mipmap.splash0,
            R.mipmap.splash1,
            R.mipmap.splash2,
            R.mipmap.splash3,
            R.mipmap.splash4,

            R.mipmap.splash6,
            R.mipmap.splash7,
            R.mipmap.splash8,
            R.mipmap.splash9,
            R.mipmap.splash10,
            R.mipmap.splash11,
            R.mipmap.splash12,
            R.mipmap.splash13,
            R.mipmap.splash14,
            R.mipmap.splash15,
            R.mipmap.splash16,
    };

    @Bind(R.id.iv_splash_bg)
    ImageView mBgImageView;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData(Bundle bundle) {
        Random r = new Random(SystemClock.elapsedRealtime());
        mBgImageView.setImageResource(SPLASH_ARRAY[r.nextInt(SPLASH_ARRAY.length)]);
        animateImage();
    }

    @Override
    protected void initPresenter() {}

    private void animateImage() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mBgImageView, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mBgImageView, "scaleY", 1f, SCALE_END);

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
}
