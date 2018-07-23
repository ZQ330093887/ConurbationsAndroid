package com.test.admin.conurbations.utils.bigImageView.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.bigImageView.ImagePreview;
import com.test.admin.conurbations.utils.bigImageView.bean.ImageInfo;
import com.test.admin.conurbations.utils.bigImageView.glide.ImageLoader;
import com.test.admin.conurbations.utils.bigImageView.glide.engine.SimpleFileTarget;
import com.test.admin.conurbations.utils.bigImageView.tool.Print;
import com.tt.whorlviewlibrary.WhorlView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImagePreviewAdapter extends PagerAdapter {

    private static final String TAG = "ImagePreview";
    private Activity activity;
    private List<ImageInfo> imageInfo;
    private HashMap<String, SubsamplingScaleImageView> imageHashMap = new HashMap<>();

    public ImagePreviewAdapter(Activity activity, @NonNull List<ImageInfo> imageInfo) {
        super();
        this.imageInfo = imageInfo;
        this.activity = activity;
    }

    public void closePage() {
        try {
            if (imageHashMap != null && imageHashMap.size() > 0) {
                for (Object o : imageHashMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    if (entry != null && entry.getValue() != null) {
                        ((SubsamplingScaleImageView) entry.getValue()).recycle();
                        Glide.clear((SubsamplingScaleImageView) entry.getValue());
                    }
                }
                imageHashMap.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }

    /**
     * 加载原图
     */
    public void loadOrigin(final String originUrl) {
        if (imageHashMap.get(originUrl) != null) {
            final SubsamplingScaleImageView imageView = imageHashMap.get(originUrl);

            Glide.with(activity).load(originUrl).downloadOnly(new SimpleFileTarget() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                }
            });
        } else {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View convertView = View.inflate(activity, R.layout.item_photoview, null);
        final WhorlView progressBar = convertView.findViewById(R.id.progress_view);
        progressBar.start();
        final SubsamplingScaleImageView imageView = convertView.findViewById(R.id.photo_view);
        imageView.setDoubleTapZoomDuration(ImagePreview.getInstance().getZoomTransitionDuration());
        imageView.setMinScale(ImagePreview.getInstance().getMinScale());
        imageView.setMaxScale(ImagePreview.getInstance().getMaxScale());
        imageView.setDoubleTapZoomScale(ImagePreview.getInstance().getMediumScale());
        imageView.setOnClickListener(v -> activity.finish());

        final ImageInfo info = this.imageInfo.get(position);
        final String originPathUrl = info.getOriginUrl();
        final String thumbPathUrl = info.getThumbnailUrl();

        if (imageHashMap.containsKey(originPathUrl)) {
            imageHashMap.remove(originPathUrl);
        }
        imageHashMap.put(originPathUrl, imageView);

        File cacheFile = ImageLoader.getGlideCacheFile(activity, originPathUrl);
        if (cacheFile != null && cacheFile.exists()) {
            Glide.with(activity).load(cacheFile).downloadOnly(new SimpleFileTarget() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                    progressBar.setVisibility(View.GONE);
                    progressBar.stop();
                }
            });
        } else {
            // 加载缩略图
            progressBar.start();
            Print.d(TAG, "thumbPathUrl == " + thumbPathUrl);
            Glide.with(activity).load(thumbPathUrl).downloadOnly(new SimpleFileTarget() {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    // 不止为何会有时候加载失败，几率挺高，在此处重新加载一次。
                    Glide.with(activity).load(thumbPathUrl).downloadOnly(new SimpleFileTarget() {
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            progressBar.setVisibility(View.GONE);
                            progressBar.stop();
                        }

                        @Override
                        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                            imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                            progressBar.setVisibility(View.GONE);
                            progressBar.stop();
                        }
                    });
                }

                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                    progressBar.setVisibility(View.GONE);
                    progressBar.stop();
                }
            });
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView((View) object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ImageLoader.clearMemory(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, final Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}