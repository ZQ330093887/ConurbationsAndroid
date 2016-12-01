package com.test.admin.conurbations.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.photoview.PhotoView;
import com.test.admin.conurbations.photoview.PhotoViewAttacher;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2016/9/26.
 */
public class BigAvatarActivity extends AppCompatActivity {

    @Bind(R.id.pv_big_avatar_avatar)
    PhotoView avatarPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_avatar);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bitmap bitmap = intent.getParcelableExtra("photoBundle");
            avatarPhotoView.setImageBitmap(bitmap);
        } else {
            avatarPhotoView.setBackgroundResource(R.mipmap.ic_launcher);
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
}
