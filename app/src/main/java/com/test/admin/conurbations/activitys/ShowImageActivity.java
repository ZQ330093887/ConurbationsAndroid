package com.test.admin.conurbations.activitys;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.photoview.PhotoView;
import com.test.admin.conurbations.photoview.PhotoViewAttacher;
import com.test.admin.conurbations.utils.CustomChangeBounds;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2016/9/26.
 */
public class ShowImageActivity extends AppCompatActivity {

    @Bind(R.id.pv_big_avatar_avatar)
    PhotoView avatarPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_avatar);
        ButterKnife.bind(this);
        ImageUtil.loadImage(getIntent().getStringExtra("position"), avatarPhotoView);

        //定义ArcMotion
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);//插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();

            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(avatarPhotoView);
            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }

        avatarPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        avatarPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        });
    }
}
