package com.test.admin.conurbations.utils.rom

import android.support.v7.app.AppCompatActivity
import com.test.admin.conurbations.R
import com.test.admin.conurbations.config.Constants
import com.test.admin.conurbations.model.Music
import com.test.admin.conurbations.model.api.MusicApiServiceImpl
import com.test.admin.conurbations.model.entity.NewsList
import com.test.admin.conurbations.model.user.UserStatus
import com.test.admin.conurbations.retrofit.ApiManager
import com.test.admin.conurbations.retrofit.RequestCallBack
import com.test.admin.conurbations.rxbus.Event
import com.test.admin.conurbations.rxbus.RxBus
import com.test.admin.conurbations.utils.ActionSheetDialog
import com.test.admin.conurbations.utils.ToastUtils
import com.test.admin.conurbations.widget.SolidApplication


/**
 * Created by master on 2018/4/8.
 * 封装添加到在线歌单功能
 */

object OnlinePlaylistUtils {
    /**
     * 保存当前歌单列表
     */
    var playlists = mutableListOf<NewsList>()

    /**
     * 获取在线歌单
     */
    fun addToPlaylist(activity: AppCompatActivity?, music: Music?) {
        if (activity == null) return
        if (music == null) {
            ToastUtils.getInstance().showToast(SolidApplication.getInstance().getString(R.string.resource_error))
            return
        }
        if (music.type == Constants.LOCAL || music.type == Constants.BAIDU) {
            ToastUtils.getInstance().showToast(SolidApplication.getInstance().getString(R.string.warning_add_playlist))
            return
        }
        if (!UserStatus.getLoginStatus()) {
            ToastUtils.getInstance().showToast(SolidApplication.getInstance().getString(R.string.prompt_login))
            return
        }
        getOnlinePlaylist(success = {
            showSelectDialog(activity, it, music)
        }, fail = {
            ToastUtils.getInstance().showToast(it)
        })
    }

    /**
     * 获取在线歌单
     */
    fun getOnlinePlaylist(success: (MutableList<NewsList>) -> Unit, fail: (String) -> Unit) {
        ApiManager.request(MusicApiServiceImpl.getPlaylist(), object : RequestCallBack<MutableList<NewsList>> {
            override fun success(result: MutableList<NewsList>) {
                playlists.clear()
                playlists.addAll(result)
                success.invoke(playlists)
            }

            override fun error(msg: String) {
                fail.invoke(msg)
            }
        })
    }

    /**
     * 删除当前歌单
     */
    fun deletePlaylist(playlist: NewsList, success: (String) -> Unit) {
        ApiManager.request(playlist.pid?.let { MusicApiServiceImpl.deletePlaylist(it) }, object : RequestCallBack<String> {
            override fun success(result: String) {
                success.invoke(result)
                ToastUtils.getInstance().showToast(result)
                RxBus.getDefault().post(Event(playlist, Constants.PLAYLIST_DELETE.toString()))
            }

            override fun error(msg: String) {
                ToastUtils.getInstance().showToast(msg)
            }
        })
    }

    /**
     * 取消收藏
     */
    fun disCollectMusic(pid: String?, music: Music?, requestBack: UIUtils.RequestBack) {
        if (pid == null) return
        if (music == null) return
        ApiManager.request(MusicApiServiceImpl.disCollectMusic(pid, music), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.getInstance().showToast(result)
                RxBus.getDefault().post(Event(NewsList().apply { this.pid = pid }, Constants.PLAYLIST_UPDATE.toString()))
                requestBack.onSuccess()
            }

            override fun error(msg: String) {
                ToastUtils.getInstance().showToast(msg)
            }
        })
    }

    /**
     * 显示歌列表
     */
    private fun showSelectDialog(activity: AppCompatActivity, playlists: MutableList<NewsList>, music: Music? = null, musicList: MutableList<Music>? = null) {
        val items = mutableListOf<String>()
        playlists.forEach {
            it.name?.let { it1 -> items.add(it1) }
        }

        ActionSheetDialog(activity)
                .builder()
                .setTitle(activity.getString(R.string.add_to_playlist))
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(items.toTypedArray(), ActionSheetDialog.SheetItemColor.Black) { which ->
                    if (playlists[which].pid == null) {
                        playlists[which].pid = playlists[which].id.toString()
                    }

                    if (musicList != null) {
                        collectBatch2Music(playlists[which], musicList)
                    } else {
                        collectMusic(playlists[which], music)
                    }
                }.show()
    }

    /**
     * 歌曲添加到在线歌单，同步
     * 目前支持网易，虾米，qq
     */
    private fun collectMusic(playlist: NewsList, music: Music?) {
        ApiManager.request(MusicApiServiceImpl.collectMusic(playlist.pid.toString(), music!!), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.getInstance().showToast(result)
                RxBus.getDefault().post(Event(playlist, Constants.PLAYLIST_UPDATE.toString()))
            }

            override fun error(msg: String) {
                ToastUtils.getInstance().showToast(msg)
            }
        })
    }

    /**
     * 歌曲批量添加到在线歌单，不同类型
     * 目前支持网易，虾米，qq
     */
    private fun collectBatch2Music(playlist: NewsList, musicList: MutableList<Music>?) {
        ApiManager.request(MusicApiServiceImpl.collectBatch2Music(playlist.pid.toString(), musicList), object : RequestCallBack<String> {
            override fun success(result: String) {
                ToastUtils.getInstance().showToast(result)
                RxBus.getDefault().post(Event(playlist, Constants.PLAYLIST_UPDATE.toString()))
            }

            override fun error(msg: String) {
                ToastUtils.getInstance().showToast(msg)
            }
        })
    }

}
