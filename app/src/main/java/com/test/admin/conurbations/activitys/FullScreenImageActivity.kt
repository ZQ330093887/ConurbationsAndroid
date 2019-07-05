package com.test.admin.conurbations.activitys

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.animation.GlideAnimation
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.test.admin.conurbations.R
import com.test.admin.conurbations.utils.DialogUtils
import com.test.admin.conurbations.utils.DisplayUtils
import com.test.admin.conurbations.utils.bigImageView.ImagePreview
import com.test.admin.conurbations.utils.bigImageView.glide.engine.SimpleFileTarget
import com.tt.whorlviewlibrary.WhorlView
import java.io.File

/**
 * 这个类比较特殊，之前显示图片的库不支持glideUrl，所以在这里重写了一个展示图片的
 * Created by ZQiong on 2017/9/11.
 */
class FullScreenImageActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_URI = "full_screen_extra_uri"
        private var currentItemOriginPathUrl = ""// 当前显示的原图链接

        fun newInstance(context: Context, uri: String): Intent {
            val intent = Intent(context, FullScreenImageActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        val progressBar = findViewById<View>(R.id.progress_view) as WhorlView
        progressBar.start()
        if (intent.extras != null) {
            val uri = intent.extras.getString(EXTRA_URI, "")
            initView()
            if (!TextUtils.isEmpty(uri)) {
                var host = ""
                if (uri.startsWith("https://")) {
                    host = uri.replace("https://", "")
                } else if (uri.startsWith("http://")) {
                    host = uri.replace("http://", "")
                }
                host = host.substring(0, host.indexOf("/"))
                val builder = LazyHeaders.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E)  Chrome/60.0.3112.90 Mobile Safari/537.36")
                        .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                        .addHeader("Host", host)
                        .addHeader("Proxy-Connection", "keep-alive")
                        .addHeader("Referer", "http://m.mzitu.com/")
                val glideUrl = GlideUrl(uri, builder.build())

                val imageView = findViewById<View>(R.id.photo_view) as SubsamplingScaleImageView

                Glide.with(this).load(glideUrl).downloadOnly(object : SimpleFileTarget() {
                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        progressBar.visibility = View.VISIBLE
                    }

                    override fun onLoadFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        // 不止为何会有时候加载失败，几率挺高，在此处重新加载一次。
                        Glide.with(this@FullScreenImageActivity).load(glideUrl).downloadOnly(object : SimpleFileTarget() {
                            override fun onLoadFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                                super.onLoadFailed(e, errorDrawable)
                                progressBar.visibility = View.GONE
                                progressBar.stop()
                            }

                            override fun onResourceReady(resource: File?, glideAnimation: GlideAnimation<in File>?) {
                                currentItemOriginPathUrl = resource?.absolutePath.toString()
                                imageView.setImage(ImageSource.uri(Uri.fromFile(File(resource?.absolutePath))))
                                progressBar.visibility = View.GONE
                                progressBar.stop()
                            }
                        })
                    }

                    override fun onResourceReady(resource: File?, glideAnimation: GlideAnimation<in File>?) {
                        currentItemOriginPathUrl = resource?.absolutePath.toString()
                        imageView.setImage(ImageSource.uri(Uri.fromFile(File(resource?.absolutePath))))
                        progressBar.visibility = View.GONE
                        progressBar.stop()
                    }
                })
                imageView.setOnClickListener {
                    finish()
                }

                imageView.setOnLongClickListener {
                    DialogUtils.showProgressDialog(this@FullScreenImageActivity)

                    val path = Environment.getExternalStorageDirectory().toString() + "/" + ImagePreview.getInstance().folderName + "/"
                    BaseActivity.downloadBitmap(this@FullScreenImageActivity, glideUrl.toStringUrl(), 1, path, System.currentTimeMillis().toString() + ".jpeg")

                    true
                }

                return
            }
        }
        finish()
    }

    private fun initView() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar?.setNavigationOnClickListener { v -> finish() }
        toolbar?.setPadding(toolbar.paddingLeft, toolbar.paddingTop + DisplayUtils.getStatusBarHeight(this), toolbar.paddingRight, toolbar.paddingBottom)
    }
}