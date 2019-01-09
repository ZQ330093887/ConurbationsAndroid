package com.test.admin.conurbations.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.databinding.ActivityTelegramGalleryBinding;

import java.util.List;

/**
 * Created by zhouqiong on 2016/9/29.
 */
public class TelegramGalleryActivity extends BaseActivity<ActivityTelegramGalleryBinding> {

    private List<String> mPhotoList;
    private BaseAdapter mBaseAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_telegram_gallery;
    }

    @Override
    protected void initData(Bundle bundle) {
        mBinding.gvTelegramGalleryContent.setAdapter(mBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mPhotoList == null ? 0 : mPhotoList.size();
            }

            @Override
            public Object getItem(int position) {
                if (mPhotoList == null) {
                    return null;
                }
                return mPhotoList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView view = new ImageView(TelegramGalleryActivity.this);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        256));
                String path = (String) getItem(position);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
                opts.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
                view.setImageBitmap(bitmap);
                return view;
            }
        });

        mBinding.btnTelegramGalleryOption.setOnClickListener(v -> onClickOptionButton());

        mBinding.btnTelegramGalleryMultipleChoice.setOnClickListener(v -> onClickCheckBoxButton());
    }

    @SuppressWarnings("all")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (12 == requestCode && resultCode == Activity.RESULT_OK) {
            mPhotoList = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
            mBaseAdapter.notifyDataSetChanged();
        }
    }

    void onClickOptionButton() {
        GalleryActivity.openActivity(TelegramGalleryActivity.this, true, 9, 12);
    }

    void onClickCheckBoxButton() {
        GalleryActivity.openActivity(TelegramGalleryActivity.this, false, 9, 12);
    }

    @Override
    public void detachView() {

    }
}
