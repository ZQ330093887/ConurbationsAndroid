package com.test.admin.conurbations.utils.rom


import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.liulishuo.filedownloader.FileDownloader
import com.test.admin.conurbations.R
import com.test.admin.conurbations.adapter.TaskItemAdapter
import com.test.admin.conurbations.config.Constants
import com.test.admin.conurbations.model.Music
import com.test.admin.conurbations.model.api.GankApi.status.success
import com.test.admin.conurbations.model.api.MusicApi
import com.test.admin.conurbations.model.db.DaoLitepal
import com.test.admin.conurbations.model.entity.NewsList
import com.test.admin.conurbations.player.playqueue.PlayHistoryLoader
import com.test.admin.conurbations.player.playqueue.PlayQueueManager
import com.test.admin.conurbations.retrofit.ApiManager
import com.test.admin.conurbations.retrofit.RequestCallBack
import com.test.admin.conurbations.rxbus.Event
import com.test.admin.conurbations.rxbus.RxBus
import com.test.admin.conurbations.utils.*
import com.test.admin.conurbations.utils.download.SongLoader
import com.test.admin.conurbations.utils.download.TasksManager
import com.test.admin.conurbations.views.AlertDialog
import com.test.admin.conurbations.widget.SolidApplication
import kotlin.concurrent.thread


object UIUtils {
    /**
     * 防止快速点击却换歌曲
     */
    private var lastClickTime: Long = 0

    @Synchronized
    fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < 500) {
            return true
        }
        lastClickTime = time
        return false
    }

    /**
     * 改变播放模式
     */
    fun updatePlayMode(imageView: ImageView, isChange: Boolean = false) {
        try {
            var playMode = PlayQueueManager.getPlayModeId()
            if (isChange) playMode = PlayQueueManager.updatePlayMode()
            when (playMode) {
                PlayQueueManager.PLAY_MODE_LOOP -> {
                    imageView.setImageResource(R.drawable.ic_repeat)
                    if (isChange) {
                        ToastUtils.getInstance().showToast(SolidApplication.getInstance().resources.getString(R.string.play_mode_loop))
                    }
                }
                PlayQueueManager.PLAY_MODE_REPEAT -> {
                    imageView.setImageResource(R.drawable.ic_repeat_one)
                    if (isChange) {
                        ToastUtils.getInstance().showToast(SolidApplication.getInstance().resources.getString(R.string.play_mode_repeat))
                    }
                }
                PlayQueueManager.PLAY_MODE_RANDOM -> {
                    imageView.setImageResource(R.drawable.ic_shuffle)
                    if (isChange) {
                        ToastUtils.getInstance().showToast(SolidApplication.getInstance().resources.getString(R.string.play_mode_random))
                    }
                }
            }
        } catch (e: Throwable) {

        }
    }

    /**
     * 收藏歌曲
     */
    fun collectMusic(imageView: ImageView, music: Music?) {
        music?.let {
            imageView.setImageResource(if (!it.isLove) R.drawable.item_favorite_love else R.drawable.item_favorite)
        }
        ValueAnimator.ofFloat(1f, 1.3f, 0.8f, 1f).apply {
            duration = 600
            addUpdateListener {
                imageView.scaleX = it.animatedValue as Float
                imageView.scaleY = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    music?.let {
                        it.isLove = SongLoader.updateFavoriteSong(it)
                        RxBus.getDefault().post(Event(null, Constants.PLAYLIST_LOVE_ID))
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }.start()
    }

    /**
     * 删除歌单
     */
    fun deletePlaylist(playlist: NewsList, context: Context): Boolean {
        var isHistory = false
        when (playlist.pid) {
            Constants.PLAYLIST_HISTORY_ID -> {

                val alertDialog = AlertDialog(context).builder()
                alertDialog.setTitle(context.getString(R.string.prompt))
                alertDialog.setMsg("是否清空播放历史？")
                alertDialog.setNegativeButton("取消", null)
                alertDialog.setPositiveButton("确认") {
                    PlayHistoryLoader.clearPlayHistory()
                    isHistory = true
                }
                alertDialog.show()
            }
            else -> {
                val alertDialog = AlertDialog(context).builder()
                alertDialog.setTitle(context.getString(R.string.prompt))
                alertDialog.setMsg("是否删除这个歌单？")
                alertDialog.setNegativeButton("取消", null)
                alertDialog.setPositiveButton("确认") {
                    PlayHistoryLoader.clearPlayHistory()
                    isHistory = false
                }
                alertDialog.show()
            }
        }
        return isHistory
    }

    /**
     * 下载歌曲
     */
    fun AppCompatActivity.downloadMusic(music: Music?, isCache: Boolean = false, context: Context) {
        if (music == null) {
            ToastUtils.getInstance().showToast(getString(R.string.download_empty_error))
            return
        }
        if (music.type == Constants.LOCAL) {
            ToastUtils.getInstance().showToast(getString(R.string.download_local_error))
            return
        }
        if (!music.isDl && !isCache) {
            ToastUtils.getInstance().showToast(getString(R.string.download_ban))
            return
        }
        ApiManager.request(MusicApi.getMusicDownloadUrl(music, isCache), object : RequestCallBack<String> {
            override fun success(result: String) {
                LogUtil.e(javaClass.simpleName, "-----${result}")
                /**
                 * 当前activity 销毁时 不显示
                 */
                if (this@downloadMusic.isDestroyed || this@downloadMusic.isFinishing) return
                if (!NetworkUtils.isWifiConnected(context) && SPUtils.getWifiMode()) {
                    AlertDialogUtils.showTipsDialog(context, getString(R.string.download_network_tips)
                    ) { _ ->
                        run {
                            music.uri = result
                            addDownloadQueue(music, isCache = isCache)
                        }
                    }
                    return
                }
                if (result.isNotEmpty() && result.startsWith("http")) {
                    AlertDialogUtils.showTipsDialog(context, getString(R.string.popup_download),
                            getString(R.string.download_content, music.title)
                    ) { _ ->
                        run {
                            music.uri = result
                            addDownloadQueue(music, isCache = isCache)
                        }
                    }
                    return
                }
                ToastUtils.getInstance().showToast(getString(R.string.download_error))
            }

            override fun error(msg: String) {
                ToastUtils.getInstance().showToast(msg)
                LogUtil.e("MUSIC_DOWN", msg)
            }
        })
    }

    /**
     * 增加到增加到下载队列
     */
    fun Context.addDownloadQueue(result: Music, isBatch: Boolean = false, isCache: Boolean = false) {
        if (result.uri == null) {
            ToastUtils.getInstance().showToast(result.title + " 下载地址异常！")
            return
        }
        if (!isBatch) {
            ToastUtils.getInstance().showToast(getString(R.string.download_add_success))
        }

        DaoLitepal.saveOrUpdateMusic(result, false)

        val path = if (isCache) FileUtils.getMusicCacheDir() + result.artist + " - " + result.title + "(" + result.quality + ")"
        else FileUtils.getMusicDir() + result.artist + " - " + result.title + ".mp3"
        val task = FileDownloader.getImpl()
                .create(result.uri)
                .setPath(path)
                .setCallbackProgressTimes(100)
                .setListener(TaskItemAdapter.taskDownloadListener)
        val model = TasksManager.addTask(task.id, result.mid, result.title, result.uri, path, isCache)
        if (model != null) {
            TasksManager.getInstance().addTaskForViewHolder(task)
            task.start()
        }
    }


    /**
     * 批量删除歌曲
     */
    fun AppCompatActivity.deleteSingleMusic(music: Music?, requestBack: RequestBack) {
        if (this.isFinishing || this.isDestroyed) return
        if (music == null) {
            AlertDialogUtils.showTipsDialog(this, getString(R.string.delete_local_song_empty))
            return
        }
        AlertDialogUtils.showTipsDialog(this, getString(R.string.delete_local_song)) {
            thread {
                SongLoader.removeSong(music)
                runOnUiThread {
                    ToastUtils.getInstance().showToast(getString(R.string.delete_song_success))
                    RxBus.getDefault().post(Event(null, Constants.PLAYLIST_LOCAL_ID))
                    requestBack.onSuccess()
                }
            }
        }
    }


    interface RequestBack {
        fun onSuccess()
    }

}