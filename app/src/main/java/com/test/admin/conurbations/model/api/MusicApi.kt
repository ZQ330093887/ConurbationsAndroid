package com.test.admin.conurbations.model.api


import com.test.admin.conurbations.config.Constants
import com.test.admin.conurbations.model.Music
import com.test.admin.conurbations.model.api.MusicApiServiceImpl.getTingSongInfo
import com.test.admin.conurbations.model.entity.SongComment
import com.test.admin.conurbations.retrofit.ApiManager
import com.test.admin.conurbations.retrofit.RequestCallBack
import com.test.admin.conurbations.utils.FileUtils
import com.test.admin.conurbations.utils.LogUtil
import com.test.admin.conurbations.utils.SPUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.HashMap

/**
 * Author   : D22434
 * version  : 2018/3/9
 * function : 接口数据集中到一个类中管理。
 */

object MusicApi {
    private val TAG = "MusicApi"

    /**
     * 获取歌词
     *
     * @param music
     * @return
     */
    fun getLyricInfo(music: Music): Observable<String>? {
        return when (music.type) {
            Constants.BAIDU -> {
                if (music.lyric != null) {
                    MusicApiServiceImpl.getBaiduLyric(music)
                } else {
                    MusicApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                        music.lyric = result.lyric
                        MusicApiServiceImpl.getBaiduLyric(music)
                    }
                }
            }
            Constants.LOCAL -> {
                MusicApiServiceImpl.getLocalLyricInfo(music)
            }
            else -> {
                MusicApiServiceImpl.getLyricInfo(music)
            }
        }
    }


    /**
     * 获取播放地址（下载）
     */
    fun getMusicDownloadUrl(music: Music, isCache: Boolean = false): Observable<String>? {
        return when (music.type) {
            Constants.BAIDU -> MusicApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                Observable.create(ObservableOnSubscribe<String> {
                    if (result.uri != null) {
                        it.onNext(result.uri!!)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                val br = if (isCache) music.quality else 128000
                MusicApiServiceImpl.getMusicUrl(music.type!!, music.mid!!, br).flatMap { result ->
                    Observable.create(ObservableOnSubscribe<String> {
                        if (result.isNotEmpty()) {
                            it.onNext(result)
                            it.onComplete()
                        } else {
                            it.onError(Throwable(""))
                        }
                    })
                }
            }
        }
    }

    /**
     * 搜索音乐具体信息（QQ音乐的播放地址会在一定的时间后失效（大概一天））
     *
     */
    fun getMusicInfo(music: Music): Observable<Music> {
        //获取默认音质
        var quality = SPUtils.getAnyByKey(Constants.SP_KEY_SONG_QUALITY, music.quality)
        //判断是否当前音质
        if (music.quality != quality) {
            quality = when {
                quality >= 999000 && music.sq -> 999000
                quality >= 320000 && music.hq -> 320000
                quality >= 192000 && music.high -> 192000
                quality >= 128000 -> 128000
                else -> 128000
            }
        }

        val cachePath = FileUtils.getMusicCacheDir() + music.artist + " - " + music.title + "(" + quality + ")"
        val downloadPath = FileUtils.getMusicDir() + music.artist + " - " + music.title + ".mp3"
        if (FileUtils.exists(cachePath)) {
            return Observable.create {
                music.uri = cachePath
                if (music.uri != null) {
                    it.onNext(music)
                    it.onComplete()
                } else {
                    it.onError(Throwable(""))
                }
            }
        } else if (FileUtils.exists(downloadPath)) {
            return Observable.create {
                music.uri = downloadPath
                if (music.uri != null) {
                    it.onNext(music)
                    it.onComplete()
                } else {
                    it.onError(Throwable(""))
                }
            }
        }
        return when (music.type) {
            Constants.BAIDU -> MusicApiServiceImpl.getTingSongInfo(music).flatMap { result ->
                Observable.create(ObservableOnSubscribe<Music> {
                    music.uri = result.uri
                    music.lyric = result.lyric
                    if (music.uri != null) {
                        it.onNext(music)
                        it.onComplete()
                    } else {
                        it.onError(Throwable(""))
                    }
                })
            }
            else -> {
                MusicApiServiceImpl.getMusicUrl(music.type ?: Constants.LOCAL, music.mid
                        ?: "", quality).flatMap { result ->
                    Observable.create(ObservableOnSubscribe<Music> {
                        music.uri = result
                        if (music.uri != null) {
                            it.onNext(music)
                            it.onComplete()
                        } else {
                            it.onError(Throwable(""))
                        }
                    })
                }

            }
        }
    }


    /**
     * 搜索歌曲评论
     */
    fun getMusicCommentInfo(music: Music, success: (MutableList<SongComment>?) -> Unit, fail: (() -> Unit?)? = null) {
        if (music.type == null || music.mid == null) {
            fail?.invoke()
            return
        }
        val observable = MusicApiServiceImpl.getMusicComment(music.type!!, music.mid!!)
        if (observable == null) {
            fail?.invoke()
            return
        }
        ApiManager.request(observable, object : RequestCallBack<MutableList<SongComment>> {
            override fun success(result: MutableList<SongComment>?) {
                success.invoke(result)
            }

            override fun error(msg: String?) {
                LogUtil.e("getMusicAlbumPic", msg)
                fail?.invoke()
            }
        })
    }


    /**
     * 加载图片
     */
    fun getMusicAlbumPic(info: String, success: (String?) -> Unit, fail: (() -> Unit?)? = null) {
        ApiManager.request(getDoubanPic(info), object : RequestCallBack<String> {
            override fun success(result: String?) {
                if (result == null) {
                    fail?.invoke()
                } else {
                    success.invoke(result)
                }
            }

            override fun error(msg: String?) {
                LogUtil.e("getMusicAlbumPic", msg)
                fail?.invoke()
            }
        })
    }

    //https://api.douban.com/v2/music/search?q=素颜&count=1
    fun getDoubanPic(info: String): Observable<String> {
        val params = HashMap<String, String>()
        params["q"] = info
        params["count"] = "10"
        //        return ApiManager.getInstance().create(DoubanApiService.class, "https://api.douban.com/")
        return ApiManager.getInstance().create(BaiduApiService::class.java, "https://douban.uieee.com/")
                .searchMusic("search", params).flatMap {
                    Observable.create(ObservableOnSubscribe<String> { e ->
                        try {
                            if (it.musics?.first() != null) {
                                e.onNext(it.musics.first().image)
                                e.onComplete()
                            } else {
                                e.onError(Throwable("网络异常"))
                            }
                        } catch (ep: Exception) {
                            e.onError(ep)
                        }
                    })
                }
    }

    /**
     * 根据路径获取本地歌词
     */
    fun getLocalLyricInfo(path: String?): Observable<String> {
        return Observable.create { emitter ->
            try {
                val lyric = FileUtils.readFile(path)
                emitter.onNext(lyric)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}