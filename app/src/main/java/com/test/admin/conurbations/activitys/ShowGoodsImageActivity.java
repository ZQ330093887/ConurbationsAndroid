package com.test.admin.conurbations.activitys;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityShowImgBinding;
import com.test.admin.conurbations.model.entity.MenuModel;
import com.test.admin.conurbations.model.entity.PageModel;
import com.test.admin.conurbations.presenter.NudeDetailPresenter;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.EventType;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.bigImageView.ImagePreview;
import com.test.admin.conurbations.utils.bigImageView.glide.engine.SimpleFileTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类比较特殊，之前显示图片的库不支持glideUrl，所以在这里重写了一个展示图片的
 * Created by ZQiong on 2017/9/11.
 */

public class ShowGoodsImageActivity extends BaseActivity<ActivityShowImgBinding> implements INudePhotosView {
    public final static String IMAGE_URL = "IMAGE_URL";
    public final static String POSITION = "POSITION";
    public final static String PAGEDATA = "PAGEDATA";
    private List<SubsamplingScaleImageView> mImageViews;
    private int mCurrentItem = 0;
    private PageModel pageModelData;
    private NudeDetailPresenter nudeDetailPresenter;
    private MyAdapter myAdapter;
    private ArrayList<String> imgUrls;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_img;
    }

    @Override
    protected void initData(Bundle bundle) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBinding.progressView.start();

        nudeDetailPresenter = new NudeDetailPresenter();
        nudeDetailPresenter.attachView(this);

        imgUrls = getIntent().getStringArrayListExtra(IMAGE_URL);
        mCurrentItem = getIntent().getIntExtra(POSITION, mCurrentItem);
        pageModelData = (PageModel) getIntent().getSerializableExtra(PAGEDATA);
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        }
        mImageViews.clear();
        if (imgUrls == null || imgUrls.size() <= 0) {
            mBinding.progressView.setVisibility(View.GONE);
            mBinding.progressView.stop();
            return;
        }

        AddView(imgUrls);
        myAdapter = new MyAdapter();
        mBinding.viewPager.setAdapter(myAdapter);
        mBinding.viewPager.setCurrentItem(mCurrentItem);

        mBinding.viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;

                if (mCurrentItem == imgUrls.size() - 1) {
                    ToastUtils.getInstance().showToast("加载中，请稍后");
                    if (nudeDetailPresenter != null) {
                        nudeDetailPresenter.getNudeDetail(pageModelData.nextPage, 2);
                    }
                }

                if (pageModelData != null) {
                    pageModelData.index = mCurrentItem;
                    RxBus.getDefault().post(new Event(pageModelData, EventType.IMAGE_POSITION));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void AddView(List<String> imgUrls) {
        for (int i = 0; i < imgUrls.size(); i++) {
            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(this);

            String uri = imgUrls.get(i);
            if (!TextUtils.isEmpty(uri)) {
                GlideUrl glideUrl = new GlideUrl(uri, new LazyHeaders.Builder().addHeader("Referer", uri).build());
                Glide.with(this).load(glideUrl).downloadOnly(new SimpleFileTarget() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        mBinding.progressView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Glide.with(ShowGoodsImageActivity.this).load(glideUrl).downloadOnly(new SimpleFileTarget() {
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                mBinding.progressView.setVisibility(View.GONE);
                                mBinding.progressView.stop();
                            }

                            @Override
                            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                                super.onResourceReady(resource, glideAnimation);
                                imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                                mBinding.progressView.setVisibility(View.GONE);
                                mBinding.progressView.stop();
                            }
                        });
                    }

                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                        mBinding.progressView.setVisibility(View.GONE);
                        mBinding.progressView.stop();
                    }
                });


                imageView.setOnClickListener(v -> finish());

                imageView.setOnLongClickListener(v -> {
                    DialogUtils.showProgressDialog(ShowGoodsImageActivity.this);

                    String path = Environment.getExternalStorageDirectory().toString() + "/" + ImagePreview.getInstance().getFolderName() + "/";
                    BaseActivity.downloadBitmap(ShowGoodsImageActivity.this,
                            glideUrl.toStringUrl(), 1, path, System.currentTimeMillis() + ".jpeg");
                    return true;
                });
            }

            mImageViews.add(imageView);
        }
    }

    @Override
    public void setNodePhotoData(List<MenuModel> list) {
    }

    @Override
    public void setNodeDetailData(PageModel pageModel) {
        ArrayList<String> newImageList = new ArrayList<>();

        if (pageModel != null) {
            pageModelData = pageModel;

            if (pageModel.itemList != null && pageModel.itemList.size() > 0) {
                for (PageModel.ItemModel itemModel : pageModel.itemList) {
                    newImageList.add(itemModel.imgUrl);
                }
            }
        }

        if (newImageList.size() > 0) {
            runOnUiThread(() -> {
                AddView(newImageList);
                myAdapter.notifyDataSetChanged();
            });

            //这里处理
            if (imgUrls == null) {
                imgUrls = new ArrayList<>();
            }
            imgUrls.addAll(newImageList);
        }
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void setCacheNudePhotos(PageModel pageModel) {
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pageModelData != null) {
            pageModelData.index = mCurrentItem;
            RxBus.getDefault().post(new Event(pageModelData, EventType.IMAGE_POSITION));
        }
    }
}
