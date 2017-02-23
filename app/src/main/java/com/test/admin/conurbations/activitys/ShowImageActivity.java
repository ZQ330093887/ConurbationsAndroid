package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.photoview.PhotoView;
import com.test.admin.conurbations.photoview.PhotoViewAttacher;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.WrapperUtils;
import com.tt.whorlviewlibrary.WhorlView;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouqiong on 2016/9/26.
 */
public class ShowImageActivity extends AppCompatActivity implements OnMenuItemClickListener {

    @Bind(R.id.pv_big_avatar_avatar)
    PhotoView avatarPhotoView;
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String TRANSIT_PIC = "picture";
    private String mImageUrl;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_avatar);
        ButterKnife.bind(this);
        parseIntent();
        initMenuFragment();
        ViewCompat.setTransitionName(avatarPhotoView, TRANSIT_PIC);
        //ImageUtil.loadImage(mImageUrl, avatarPhotoView);
        //Picasso.with(this).load(mImageUrl).into(avatarPhotoView);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lly_progress_bar);
        final WhorlView whorlView = (WhorlView) findViewById(R.id.whorl);
        whorlView.start();

        Glide.with(this)
                .load(mImageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        whorlView.stop();
                        Toast.makeText(getApplicationContext(), "资源加载异常", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        whorlView.stop();
                        linearLayout.setVisibility(View.GONE);
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
                Glide.get(view.getContext()).clearMemory();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_wallpaper) {
            mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
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

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (position == 1 || position == 4 || position == 5) {
            DialogUtils.showProgressDialog(ShowImageActivity.this);
        }
        downloadBitmap(ShowImageActivity.this, mImageUrl, position);
    }


    public void downloadBitmap(final Context context, String url, final int state) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                if (state == 1) {
                    //保存图片
                    SaveBitmapUtils.getSaveBitmapObservable(bitmap).subscribe(SaveBitmapUtils.saveSubscriber);
                } else if (state == 2) {
                    //分享图片
                    SaveBitmapUtils.getSaveBitmapObservable(bitmap).subscribe(SaveBitmapUtils.getShareSubscriber(ShowImageActivity.this));
                } else if (state == 4) {
                    //设置桌面壁纸
                    WrapperUtils.getSetWallWrapperObservable(bitmap, context).subscribe(WrapperUtils.callbackSubscriber);
                } else if (state == 5) {
                    //设置锁屏壁纸
                    WrapperUtils.getSetLockWrapperObservable(bitmap, context).subscribe(WrapperUtils.callbackSubscriber);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                ToastUtils.getInstance().showToast("操作失败！");
                DialogUtils.hideProgressDialog();
            }
        });
    }
}