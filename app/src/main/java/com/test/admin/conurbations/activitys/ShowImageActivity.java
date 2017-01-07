package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.photoview.PhotoView;
import com.test.admin.conurbations.photoview.PhotoViewAttacher;
import com.test.admin.conurbations.utils.imageUtils.ImageUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2016/9/26.
 */
public class ShowImageActivity extends AppCompatActivity {

    @Bind(R.id.pv_big_avatar_avatar)
    PhotoView avatarPhotoView;
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String TRANSIT_PIC = "picture";
    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_avatar);
        ButterKnife.bind(this);
        parseIntent();
        ViewCompat.setTransitionName(avatarPhotoView, TRANSIT_PIC);
        ImageUtil.loadImage(mImageUrl, avatarPhotoView);
        //Picasso.with(this).load(mImageUrl).into(avatarPhotoView);

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

    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    }

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, ShowImageActivity.class);
        intent.putExtra(ShowImageActivity.EXTRA_IMAGE_URL, url);
        return intent;
    }
}
