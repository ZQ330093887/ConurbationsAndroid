package com.test.admin.conurbations.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.test.admin.conurbations.utils.DialogUtils;
import com.tt.whorlviewlibrary.WhorlView;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import butterknife.Bind;

/**
 * Created by zhouqiong on 2016/9/26.
 */
public class ShowImageActivity extends BaseActivity implements OnMenuItemClickListener {

    @Bind(R.id.toolbar_show_image_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pv_show_image_photo)
    PhotoView mPhotoView;
    @Bind(R.id.lly_show_image_progress_bar)
    LinearLayout mProgressBarLinearLayout;
    @Bind(R.id.wv_show_image_progress_bar)
    WhorlView mProgressBarWhorlView;
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String TRANSIT_PIC = "picture";
    private String mImageUrl;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_show_image;
    }

    @Override
    protected void initData(Bundle bundle) {
        mProgressBarWhorlView.start();
        initToolbar(mToolbar, "美图", "");
        parseIntent();
        initMenuFragment();
        ViewCompat.setTransitionName(mPhotoView, TRANSIT_PIC);
        //ImageUtil.loadImage(mImageUrl, avatarPhotoView);
        //Picasso.with(this).load(mImageUrl).into(avatarPhotoView);

        Glide.with(this)
                .load(mImageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mProgressBarWhorlView.stop();
                        mProgressBarLinearLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "资源加载异常", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mProgressBarWhorlView.stop();
                        mProgressBarLinearLayout.setVisibility(View.GONE);
                        mPhotoView.setVisibility(View.VISIBLE);
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
                .into(mPhotoView);

        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Glide.get(view.getContext()).clearMemory();
                finish();
            }
        });
    }

    @Override
    protected void initPresenter() {
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
}