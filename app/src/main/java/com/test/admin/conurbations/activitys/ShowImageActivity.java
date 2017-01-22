package com.test.admin.conurbations.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.photoview.PhotoView;
import com.test.admin.conurbations.photoview.PhotoViewAttacher;

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
        //ImageUtil.loadImage(mImageUrl, avatarPhotoView);
        //Picasso.with(this).load(mImageUrl).into(avatarPhotoView);
        final Dialog loadingDialog = createLoadingDialog(this);
        loadingDialog.show();

        Glide.with(this)
                .load(mImageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "资源加载异常", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loadingDialog.dismiss();
                        return false;
                    }
                })
                .centerCrop()
                .placeholder(R.color.white)
                .crossFade()
                .override(600, 600)
                .skipMemoryCache(true)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(avatarPhotoView);

        avatarPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        avatarPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Glide.get(view.getContext()).clearDiskCache();
                Glide.get(view.getContext()).clearMemory();
                finish();
            }
        });
    }

    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    }

    public static Dialog createLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);

        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;

    }

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, ShowImageActivity.class);
        intent.putExtra(ShowImageActivity.EXTRA_IMAGE_URL, url);
        return intent;
    }
}
