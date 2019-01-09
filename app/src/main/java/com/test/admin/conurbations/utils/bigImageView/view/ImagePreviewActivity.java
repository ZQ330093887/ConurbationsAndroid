package com.test.admin.conurbations.utils.bigImageView.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.databinding.ActivityImagePreviewBinding;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.bigImageView.ImagePreview;
import com.test.admin.conurbations.utils.bigImageView.bean.ImageInfo;
import com.test.admin.conurbations.utils.bigImageView.glide.ImageLoader;
import com.test.admin.conurbations.utils.bigImageView.glide.engine.ProgressTarget;
import com.test.admin.conurbations.utils.bigImageView.tool.DownloadPictureUtil;
import com.test.admin.conurbations.utils.bigImageView.tool.HandlerUtils;
import com.test.admin.conurbations.utils.bigImageView.tool.Print;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.File;
import java.util.List;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class ImagePreviewActivity extends BaseActivity<ActivityImagePreviewBinding> implements Handler.Callback,
        View.OnClickListener, OnMenuItemClickListener {

    public static final String TAG = "ImagePreview";

    private List<ImageInfo> imageInfoList;
    private int currentItem;// 当前显示的图片索引
    private String downloadFolderName = "";// 保存的文件夹名
    private boolean isShowDownButton;
    private boolean isShowOriginButton;

    private ImagePreviewAdapter imagePreviewAdapter;

    private String currentItemOriginPathUrl = "";// 当前显示的原图链接
    private HandlerUtils.HandlerHolder handlerHolder;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initData(Bundle bundle) {
        initToolbar(mBinding.toolbarImagePreviewToolbar, "美图", "");
        initMenuFragment();
        handlerHolder = new HandlerUtils.HandlerHolder(this);

        imageInfoList = ImagePreview.getInstance().getImageInfoList();
        currentItem = ImagePreview.getInstance().getIndex();
        downloadFolderName = ImagePreview.getInstance().getFolderName();
        isShowDownButton = ImagePreview.getInstance().isShowDownButton();
        isShowOriginButton = ImagePreview.getInstance().isShowOriginButton();

        currentItemOriginPathUrl = imageInfoList.get(currentItem).getOriginUrl();

        if (isShowOriginButton) {
            // 检查缓存是否存在
            checkCache(currentItemOriginPathUrl);
        }

        // 查看与原图按钮
        mBinding.tvImagePreviewShowOrigin.setOnClickListener(this);
        // 下载图片按钮
        mBinding.ivImagePreviewDownload.setOnClickListener(this);

        if (imageInfoList.size() > 1) {
            mBinding.tvImagePreviewIndicator.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvImagePreviewIndicator.setVisibility(View.GONE);
        }

        if (isShowDownButton) {
            mBinding.ivImagePreviewDownload.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivImagePreviewDownload.setVisibility(View.GONE);
        }

        // 更新进度指示器
        mBinding.tvImagePreviewIndicator.setText(
                String.format(getString(R.string.indicator), currentItem + 1 + " ", " " + imageInfoList.size()));

        imagePreviewAdapter = new ImagePreviewAdapter(this, imageInfoList);

        mBinding.hvpImagePreviewPager.setAdapter(imagePreviewAdapter);
        mBinding.hvpImagePreviewPager.setCurrentItem(currentItem);
        mBinding.hvpImagePreviewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                currentItemOriginPathUrl = imageInfoList.get(position).getOriginUrl();
                if (isShowOriginButton) {
                    // 检查缓存是否存在
                    checkCache(currentItemOriginPathUrl);
                }
                // 更新进度指示器
                mBinding.tvImagePreviewIndicator.setText(
                        String.format(getString(R.string.indicator), currentItem + 1 + " ", " " + imageInfoList.size()));
            }
        });
    }

    /**
     * 下载当前图片到SD卡
     */
    private void downloadCurrentImg() {
        String path = Environment.getExternalStorageDirectory() + "/" + downloadFolderName + "/";
        DownloadPictureUtil.downloadPicture(getContext(), currentItemOriginPathUrl, path,
                System.currentTimeMillis() + ".jpeg");
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
    public boolean handleMessage(Message msg) {
        if (msg.what == 0) {// 点击查看原图按钮，开始加载原图
            final String path = imageInfoList.get(currentItem).getOriginUrl();
            Print.d(TAG, "handler == 0 path = " + path);
            visible();
            mBinding.tvImagePreviewShowOrigin.setText("0 %");

            Glide.with(this).load(path).downloadOnly(new ProgressTarget<String, File>(path, null) {
                @Override
                public void onProgress(String url, long bytesRead, long expectedLength) {
                    int progress = (int) ((float) bytesRead * 100 / (float) expectedLength);
                    Print.d(TAG, "OnProgress--->" + progress);

                    if (bytesRead == expectedLength) {// 加载完成
                        Message message = handlerHolder.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        message.what = 1;
                        message.obj = bundle;
                        handlerHolder.sendMessage(message);
                    } else {// 加载中
                        Message message = handlerHolder.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        bundle.putInt("progress", progress);
                        message.what = 2;
                        message.obj = bundle;
                        handlerHolder.sendMessage(message);
                    }
                }

                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> animation) {
                    super.onResourceReady(resource, animation);
                    Message message = handlerHolder.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", path);
                    message.what = 1;
                    message.obj = bundle;
                    handlerHolder.sendMessage(message);
                }

                @Override
                public void getSize(SizeReadyCallback cb) {
                    cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                }
            });
        } else if (msg.what == 1) {// 加载完成
            Print.d(TAG, "handler == 1");
            Bundle bundle = (Bundle) msg.obj;
            String url = bundle.getString("url");
            if (currentItem == getRealIndexWithPath(url)) {
                gone();
                imagePreviewAdapter.loadOrigin(url);
            }
        } else if (msg.what == 2) {// 加载中
            Bundle bundle = (Bundle) msg.obj;
            String url = bundle.getString("url");
            int progress = bundle.getInt("progress");
            if (currentItem == getRealIndexWithPath(url)) {
                visible();
                mBinding.tvImagePreviewShowOrigin.setText(progress + " %");
                Print.d(TAG, "handler == 2 progress == " + progress);
            }
        } else if (msg.what == 3) {// gone
            mBinding.tvImagePreviewShowOrigin.setText("查看原图");
            mBinding.flImagePreviewImage.setVisibility(View.GONE);
        } else if (msg.what == 4) {// visible
            mBinding.flImagePreviewImage.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private int getRealIndexWithPath(String path) {
        for (int i = 0; i < imageInfoList.size(); i++) {
            if (path.equalsIgnoreCase(imageInfoList.get(i).getOriginUrl())) {
                return i;
            }
        }
        return 0;
    }

    private void checkCache(final String url_) {
        gone();
        new Thread(() -> {
            File cacheFile = ImageLoader.getGlideCacheFile(getContext(), url_);
            if (cacheFile != null && cacheFile.exists()) {
                gone();
            } else {
                visible();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_image_preview_download) {// 检查权限
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ImagePreviewActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 拒绝权限
                    ToastUtils.getInstance().showToast("您拒绝了存储权限，下载失败！");
                } else {
                    //申请权限
                    ActivityCompat.requestPermissions(ImagePreviewActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
                }
            } else {
                // 下载当前图片
                downloadCurrentImg();
            }
        } else if (i == R.id.tv_image_preview_show_origin) {
            handlerHolder.sendEmptyMessage(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    downloadCurrentImg();
                } else {
                    ToastUtils.getInstance().showToast("您拒绝了存储权限，下载失败！");
                }
            }
        }
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
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.dimens_50_dp));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePreviewAdapter != null) {
            imagePreviewAdapter.closePage();
        }
        ImagePreview.getInstance().reset();
    }

    private void gone() {
        handlerHolder.sendEmptyMessage(3);
        Print.d(TAG, "------gone------");
    }

    private void visible() {
        handlerHolder.sendEmptyMessage(4);
        Print.d(TAG, "------visible------");
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (position == 1 || position == 4 || position == 5) {
            DialogUtils.showProgressDialog(ImagePreviewActivity.this);
        }

        String path = Environment.getExternalStorageDirectory() + "/" + downloadFolderName + "/";
        downloadBitmap(ImagePreviewActivity.this, currentItemOriginPathUrl, position, path, System.currentTimeMillis() + ".jpeg");
    }
}