package com.test.admin.conurbations.utils.imageUtils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public abstract class ImageCallback {

	protected ImageView imageView;

	public ImageCallback(ImageView imageView) {
		this.imageView = imageView;
	}

	abstract void imageLoaded(Drawable imageDrawable, String url);

	abstract void imageLoaded(Bitmap bitmap, String url);
}