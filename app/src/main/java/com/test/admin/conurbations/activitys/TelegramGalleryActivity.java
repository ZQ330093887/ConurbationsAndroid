package com.test.admin.conurbations.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.test.admin.conurbations.R;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhouqiong on 2016/9/29.
 */
public class TelegramGalleryActivity extends BaseActivity {
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.gv)
    GridView gv;
    private List<String> photos;
    private BaseAdapter adapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_telegram_gallery;
    }

    @Override
    protected void initData(Bundle bundle) {

        gv.setAdapter(adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return photos == null ? 0 : photos.size();
            }

            @Override
            public Object getItem(int position) {
                if (photos == null) {
                    return null;
                }
                return photos.get(position);
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
    }

    @Override
    protected void initPresenter() {

    }

    @SuppressWarnings("all")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (12 == requestCode && resultCode == Activity.RESULT_OK) {
            photos = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.btn, R.id.btn2})
    void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn){
            GalleryActivity.openActivity(TelegramGalleryActivity.this, false, 9, 12);
        }else if (id == R.id.btn2){
            GalleryActivity.openActivity(TelegramGalleryActivity.this, true, 9, 12);
        }
    }
}
