package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.photoview.PhotoView;
import com.test.admin.conurbations.photoview.PhotoViewAttacher;

import butterknife.Bind;

/**
 * Created by zhouqiong on 2016/9/26.
 */
public class BigAvatarActivity extends BaseActivity {

    @Bind(R.id.pv_big_avatar_avatar)
    PhotoView avatarPhotoView;
    @Bind(R.id.toolbar_big_show_image)
    Toolbar toolbar;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_big_avatar;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(toolbar, getString(R.string.app_name), "");
        Intent intent = getIntent();
        if (intent != null) {
            Bitmap bitmap = intent.getParcelableExtra("photoBundle");
            avatarPhotoView.setImageBitmap(bitmap);
        } else {
            avatarPhotoView.setBackgroundResource(R.color.white);
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
            }
        });
    }

    @Override
    protected void initPresenter() {

    }
}
