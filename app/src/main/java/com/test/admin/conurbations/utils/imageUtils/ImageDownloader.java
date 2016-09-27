package com.test.admin.conurbations.utils.imageUtils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.LruCache;


import com.test.admin.conurbations.config.Constants;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImageDownloader {
	private Map<String, SoftReference<Drawable>> imageCache = new HashMap<>();

	private LruCache<String, Bitmap> memoryCache;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(8);
	
	private final Handler handler = new Handler();

	private static ImageDownloader instance = new ImageDownloader();

	private ImageDownloader() {
//		long maxMemory = Runtime.getRuntime().maxMemory();
//		int cacheSize = (int) maxMemory / 8;
//		memoryCache = new LruCache<String, Bitmap>(cacheSize) {
//			@Override
//			protected int sizeOf(String key, Bitmap value) {
//				return value.getByteCount();
//			}
//		};
	}

	public static ImageDownloader instance() {
		return instance;
	}
	
	public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback){
		Drawable drawable;
		final String fileName = imageUrl.hashCode() + ".jpg";
		
		if (imageCache.containsKey(fileName)){
			SoftReference<Drawable> softReference = imageCache.get(fileName);
			drawable = softReference.get();
			if (drawable != null){
				imageCallback.imageLoaded(drawable, imageUrl);
				return drawable;
			}
		}

		executorService.submit(new LoadTask(fileName, imageUrl, imageCallback));
		
		return null;
	}

	class LoadTask implements Runnable{
	
		private String fileName;
		private String imageUrl;
		private ImageCallback imageCallback;
		private Drawable drawable = null;
		
		public LoadTask(String fileName, String imageUrl, ImageCallback imageCallback) {
			this.fileName = fileName;
			this.imageUrl = imageUrl;
			this.imageCallback = imageCallback;
		}
		
		@Override
		public void run() {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File dir = new File(Environment.getExternalStorageDirectory() + Constants.IMAGE_CACHE_FOLDER_PATH);
				File file;
				
				if (!dir.exists())
					dir.mkdirs();
				
				file = new File(dir, fileName);
				if (file.exists() && file.length() > 0){
					drawable = readFromSdcard(file);
				}
				else{
					saveFile(file, imageUrl);
					drawable = loadImageFromUrl(imageUrl);
				}
			}
			else{
				drawable = loadImageFromUrl(imageUrl);
			}
			
			imageCache.put(fileName, new SoftReference<>(drawable));
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					imageCallback.imageLoaded(drawable, imageUrl);
				}
			});
		}
		
		private void saveFile(File file, String imageUrl){
			try {
				URL url = new URL(imageUrl);
				InputStream inputStream = url.openStream();
				DataInputStream dataInputStream = new DataInputStream(inputStream);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int byteRead;
				
				while ((byteRead = dataInputStream.read(buffer)) != -1)
					fileOutputStream.write(buffer, 0, byteRead);
				
				inputStream.close();
				fileOutputStream.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		private Drawable readFromSdcard(File file){
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				return Drawable.createFromStream(fileInputStream, file.getName());
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		private Drawable loadImageFromUrl(String imageUrl){
			try {
				URL url = new URL(imageUrl);
				return Drawable.createFromStream(url.openStream(), "image.jpg");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
}
