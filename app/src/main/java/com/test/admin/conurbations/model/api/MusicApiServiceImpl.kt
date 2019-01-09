package com.test.admin.conurbations.model.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.admin.conurbations.R
import com.test.admin.conurbations.config.Constants
import com.test.admin.conurbations.model.Music
import com.test.admin.conurbations.model.MusicInfo
import com.test.admin.conurbations.model.entity.*
import com.test.admin.conurbations.model.user.UserStatus
import com.test.admin.conurbations.retrofit.ApiManager
import com.test.admin.conurbations.utils.FileUtils
import com.test.admin.conurbations.utils.LogUtil
import com.test.admin.conurbations.utils.MusicUtils
import com.test.admin.conurbations.utils.download.SongLoader
import com.test.admin.conurbations.widget.SolidApplication
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by ZQiong on 2018/12/9.
 */
object MusicApiServiceImpl {
    private val apiService by lazy { ApiManager.getInstance().create(BaiduApiService::class.java, Constants.BASE_URL_BAIDU_MUSIC) }
    val token: String?
        get() = UserStatus.getUserInfo()?.token

    /**
     * 获取专辑单曲
     *
     */
    fun getAlbumSongs(vendor: String, id: String): Observable<MutableList<Music>> {
        return Observable.create { result ->
//            BaseApiImpl
//                    .getAlbumDetail(vendor, id, {
//                        if (it.status) {
//                            val musicList = arrayListOf<Music>()
//                            it.data.songs.forEach {
//                                it.vendor = vendor
//                                musicList.add(MusicUtils.getMusic(it))
//                            }
//                            result.onNext(musicList)
//                            result.onComplete()
//                        } else {
//                            result.onError(Throwable(""))
//                        }
//                    }, {})
        }
    }

    /**
     * 获取歌手单曲
     *
     */
    fun getArtistSongs(vendor: String, id: String, offset: Int = 0, limit: Int = 20): Observable<Artist> {
        return Observable.create { result ->
//            BaseApiImpl
//                    .getArtistSongs(vendor, id, offset, limit, {
//                        if (it.status) {
//                            val musicList = arrayListOf<Music>()
//                            it.data.songs.forEach {
//                                if (!it.cp) {
//                                    it.vendor = vendor
//                                    musicList.add(MusicUtils.getMusic(it))
//                                }
//                            }
//                            val artist = Artist()
//                            artist.songs = musicList
//                            artist.name = it.data.detail.name
//                            artist.picUrl = it.data.detail.cover
//                            artist.desc = it.data.detail.desc
//                            artist.artistId = it.data.detail.id
//                            result.onNext(artist)
//                            result.onComplete()
//                        } else {
//                            result.onError(Throwable(""))
//                        }
//                    }, {})
        }
    }

    /**
     * 获取歌曲url信息
     * @param br 音乐品质
     *
     */
    fun getMusicUrl(vendor: String, mid: String, br: Int = 128000): Observable<String> {
        LogUtil.d("getMusicUrl $vendor $mid $br")
        return Observable.create { result ->
//            BaseApiImpl.getSongUrl(vendor, mid, br, {
//                if (it.status) {
//                    val url =
//                            if (vendor == Constants.XIAMI) {
//                                if (it.data.url.startsWith("http")) it.data.url
//                                else "http:${it.data.url}"
//                            } else it.data.url
//                    result.onNext(url)
//                    result.onComplete()
//                } else {
//                    result.onError(Throwable(it.msg))
//                }
//            }, {})
        }
    }

    /**
     * 获取歌单详情
     * "http://music.baidu.com/data/music/links?songIds=$mid"
     */
    fun getTingSongInfo(music: Music): Observable<Music> {
        val url = Constants.URL_GET_SONG_INFO + music.mid
        return apiService.getTingSongInfo(url)
                .flatMap { data ->
                    val music = Music()
                    val songInfo = data.data.songList?.get(0)
                    songInfo?.let {
                        music.type = Constants.BAIDU
                        music.isOnline = true
                        music.mid = songInfo.songId.toString()
                        music.album = songInfo.albumName
                        music.albumId = songInfo.albumId.toString()
                        music.artistId = songInfo.artistId
                        music.artist = songInfo.artistName
                        music.title = songInfo.songName
                        music.uri = songInfo.songLink
                        music.fileSize = songInfo.size.toLong()
                        music.lyric = songInfo.lrcLink
                        music.coverSmall = songInfo.songPicSmall
                        music.coverUri = songInfo.songPicBig
                        music.coverBig = songInfo.songPicRadio.split("@")[0]

                    }
                    Observable.create(ObservableOnSubscribe<Music> { e ->
                        if (music.uri != null) {
                            SongLoader.updateMusic(music)
                            e.onNext(music)
                            e.onComplete()
                        } else {
                            e.onError(Throwable())
                        }
                    })
                }
    }

    /**
     * 获取歌词
     *
     */
    fun getLyricInfo(music: Music): Observable<String>? {
        try {
            val mLyricPath = FileUtils.getLrcDir() + "${music.title}-${music.artist}" + ".lrc"
            val vendor = music.type!!
            val mid = music.mid!!
            //网络歌词
            return if (FileUtils.exists(mLyricPath)) {
                MusicApi.getLocalLyricInfo(mLyricPath)
            } else Observable.create { result ->
//                BaseApiImpl.getLyricInfo(vendor, mid) {
//                    if (it.status) {
//                        val lyricInfo = it.data.lyric
//                        val lyric = StringBuilder()
//                        lyricInfo.forEach {
//                            lyric.append(it)
//                            lyric.append("\n")
//                        }
//                        it.data.translate.forEach {
//                            lyric.append(it)
//                            lyric.append("\n")
//                        }
//                        //保存文件
//                        val save = FileUtils.writeText(mLyricPath, lyric.toString())
//                        LogUtil.e("保存网络歌词：$save")
//                        Observable.fromArray(lyric)
//                        result.onNext(lyric.toString())
//                        result.onComplete()
//                    } else {
//                        result.onError(Throwable(""))
//                    }
//                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 获取歌词
     */
    fun getBaiduLyric(music: Music): Observable<String>? {
        //本地歌词路径
        val mLyricPath = FileUtils.getLrcDir() + music.title + "-" + music.artist + ".lrc"
        //网络歌词
        val mLyricUrl = music.lyric
        return if (FileUtils.exists(mLyricPath)) {
            Observable.create { emitter ->
                try {
                    val lyric = FileUtils.readFile(mLyricPath)
                    emitter.onNext(lyric)
                    emitter.onComplete()
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
        } else if (mLyricUrl != null) {
            apiService.getBaiduLyric(mLyricUrl)
                    .flatMap { baiDuLyricInfo ->
                        val lyric = baiDuLyricInfo.string()
                        //保存文件
                        val save = FileUtils.writeText(mLyricPath, lyric)
                        LogUtil.e("保存网络歌词：$save")
                        Observable.fromArray(lyric)
                    }
        } else {
            null
        }
    }

    /**
     * 获取歌曲url信息
     *
     */
    fun getMusicComment(vendor: String, mid: String): Observable<MutableList<SongComment>>? {
//        return when (vendor) {
//            Constants.NETEASE -> Observable.create { result ->
//                BaseApiImpl.getComment(vendor, mid, {
//                    it as SongCommentData<NeteaseComment>
//                    if (it.status) {
//                        val comments = mutableListOf<SongComment>()
//                        it.data?.comments?.forEach {
//                            val songComment = SongComment().apply {
//                                avatarUrl = it.user.avatarUrl
//                                nick = it.user.nickname
//                                commentId = it.commentId.toString()
//                                time = it.time
//                                userId = it.user.userId
//                                content = it.content
//                                type = Constants.NETEASE
//                            }
//                            comments.add(songComment)
//                        }
//                        result.onNext(comments)
//                        result.onComplete()
//                    } else {
//                        result.onError(Throwable(""))
//                    }
//                }, {})
//            }
//            Constants.QQ -> Observable.create { result ->
//                BaseApiImpl
//                        .getComment(vendor, mid, {
//                            it as SongCommentData<QQComment>
//                            if (it.status) {
//                                val comments = mutableListOf<SongComment>()
//                                it.data?.comments?.forEach {
//                                    val songComment = SongComment().apply {
//                                        avatarUrl = it.avatarurl
//                                        nick = it.nick
//                                        commentId = it.rootcommentid
//                                        time = it.time * 1000
//                                        userId = it.uin
//                                        content = it.rootcommentcontent
//                                        type = Constants.QQ
//                                    }
//                                    comments.add(songComment)
//                                }
//                                result.onNext(comments)
//                                result.onComplete()
//                            } else {
//                                result.onError(Throwable(""))
//                            }
//                        }, {})
//            }
//            Constants.XIAMI -> Observable.create { result ->
//                BaseApiImpl
//                        .getComment(vendor, mid, {
//                            it as SongCommentData<XiamiComment>
//                            if (it.status) {
//                                val comments = mutableListOf<SongComment>()
//                                it.data?.comments?.forEach {
//                                    val songComment = SongComment().apply {
//                                        avatarUrl = it.avatar
//                                        nick = it.nickName
//                                        commentId = it.commentId.toString()
//                                        time = it.gmtCreate
//                                        userId = it.userId.toString()
//                                        content = it.message
//                                        type = Constants.XIAMI
//                                    }
//                                    comments.add(songComment)
//                                }
//                                result.onNext(comments)
//                                result.onComplete()
//                            } else {
//                                result.onError(Throwable(""))
//                            }
//                        }, {})
//            }
//            else -> null
//        }
        return null
    }


    /**
     * 获取本地歌词
     */
    fun getLocalLyricInfo(music: Music): Observable<String> {
        val mLyricPath = FileUtils.getLrcDir() + music.title + "-" + music.artist + ".lrc"
        //网络歌词
        return MusicApi.getLocalLyricInfo(mLyricPath)
    }

    /**
     * 保存歌词
     */
    fun saveLyricInfo(name: String, artist: String, lyricInfo: String) {
        val mLyricPath = FileUtils.getLrcDir() + "$name-$artist.lrc"
        val save = FileUtils.writeText(mLyricPath, lyricInfo)
        LogUtil.e("保存网络歌词：$save")
    }

    /**
     * 获取歌词
     */
    fun getKugouLyricInfo(candidates: Candidates?): Observable<String>? {
        if (candidates?.id == null) {
            return null
        }
        return Observable.create { result ->
            apiService.getRawLyric(candidates.id, candidates.accesskey).subscribe {
                if (it?.status == 200) {
                    val tt = FileUtils.decryptBASE64(it.content)
                    result.onNext(tt)
                    result.onComplete()
                } else {
                    result.onError(Throwable())
                }
            }
        }
    }

    /**
     * 获取全部歌单
     */
    fun getPlaylist(): Observable<MutableList<NewsList>> {
        return apiService.getOnlinePlaylists(token)
                .flatMap { it ->
                    val json = it.string()
                    val data = Gson().fromJson<MutableList<PlaylistInfo>>(json, object : TypeToken<MutableList<PlaylistInfo>>() {
                    }.type)
                    val result = mutableListOf<NewsList>()
                    for (playlistInfo in data) {
                        val playlist = NewsList()
                        playlist.pid = playlistInfo.id
                        playlist.name = playlistInfo.name
                        playlist.total = playlistInfo.total.toLong()
                        playlist.coverUrl = playlistInfo.cover
                        playlist.type = Constants.PLAYLIST_CUSTOM_ID
                        result.add(playlist)
                    }
                    Observable.create(ObservableOnSubscribe<MutableList<NewsList>> {
                        if (data.isEmpty() && json.contains("msg")) {
                            val msg = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                            it.onError(Throwable(msg.msg))
                        } else {
                            it.onNext(result)
                            it.onComplete()
                        }
                    })
                }
    }

    /**
     * 删除歌单
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun deletePlaylist(pid: String): Observable<String> {
        return apiService.deleteMusic(token, pid)
                .flatMap { it ->
                    val json = it.string()
                    val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                    Observable.create(ObservableOnSubscribe<String> {
                        if (errorInfo.msg.isEmpty()) {
                            it.onNext("歌单删除成功")
                            it.onComplete()
                        } else {
                            it.onError(Throwable(errorInfo.msg))
                        }
                    })
                }
    }

    /**
     * 重命名歌单
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun renamePlaylist(pid: String, name: String): Observable<String> {
        val playlist = PlaylistInfo()
        playlist.name = name
        return apiService.renameMusic(token, pid, playlist)
                .flatMap { it ->
                    val json = it.string()
                    val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                    Observable.create(ObservableOnSubscribe<String> {
                        if (errorInfo.msg.isEmpty()) {
                            it.onNext("修改成功")
                            it.onComplete()
                        } else {
                            it.onError(Throwable(errorInfo.msg))
                        }
                    })
                }
    }

    /**
     * 取消收藏
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun disCollectMusic(pid: String, music: Music): Observable<String> {
        return apiService.disCollectMusic(token, pid, music.collectId.toString())
                .flatMap { it ->
                    val json = it.string()
                    val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                    Observable.create(ObservableOnSubscribe<String> {
                        if (errorInfo.msg.isEmpty()) {
                            it.onNext("已取消收藏")
                            it.onComplete()
                        } else {
                            it.onError(Throwable(errorInfo.msg))
                        }
                    })
                }
    }


    /**
     * 搜索歌词
     */
    fun searchLyric(title: String, duration: Long): Observable<Candidates.KugouLyric> {
        return apiService.searchLyric(title, duration.toString())
    }

    /**
     * 收藏歌曲
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun collectMusic(pid: String, music: Music): Observable<String>? {
        val musicInfo = MusicUtils.getMusicInfo(music)
        return apiService.collectMusic(token, pid, musicInfo)
                .flatMap { it ->
                    val json = it.string()
                    Observable.create(ObservableOnSubscribe<String> {
                        if (json == "{}") {
                            it.onNext("添加成功")
                            it.onComplete()
                        } else {
                            try {
                                val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                                it.onNext(errorInfo.msg)
                                it.onComplete()
                            } catch (e: Throwable) {
                                it.onError(Throwable(e.message))
                            }
                        }
                    })
                }
    }

    /**
     * 批量收藏歌曲（不同歌单）
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun collectBatch2Music(pid: String, musicList: MutableList<Music>?): Observable<String>? {
        val collects = mutableListOf<MusicInfo.CollectDetail>()
        musicList?.forEach {
            it.type?.let { it1 -> it.mid?.let { it2 -> MusicInfo.CollectDetail(it2, it1) } }?.let { it2 -> collects.add(it2) }
        }
        return apiService.collectBatch2Music(token, pid, MusicInfo.CollectBatch2Bean(collects))
                .flatMap { result ->
                    Observable.create(ObservableOnSubscribe<String> {
                        if (result.failedList != null) {
                            it.onNext("${musicList!!.size - result.failedList!!.size}首添加成功,${result.failedList!!.size}首添加失败！")
                            it.onComplete()
                        } else {
                            it.onError(Throwable("添加失败"))
                        }
                    })
                }
    }


    /**
     * 通过百度搜索,获取MusicInfo
     */
    fun getSearchMusicInfo(query: String, limit: Int, offset: Int): Observable<MutableList<Music>> {
        return apiService.queryMerge(query, offset, limit)
                .flatMap {
                    Observable.create(ObservableOnSubscribe<MutableList<Music>> { e ->
                        val musicList = mutableListOf<Music>()
                        try {
                            if (it.errorCode == 22000) {
                                it.result.songInfo.songList?.forEach { song ->
                                    val musicInfo = Music()
                                    musicInfo.mid = song.songId
                                    musicInfo.type = Constants.BAIDU
                                    musicInfo.title = song.title
                                    musicInfo.artist = song.author
                                    musicInfo.artistId = song.allArtistId
                                    musicInfo.album = song.albumTitle
                                    musicInfo.albumId = song.albumId
                                    musicInfo.coverUri = song.picSmall
                                    musicList.add(musicInfo)
                                }
                            }
                        } catch (error: Exception) {
                            e.onError(Throwable(error.message))
                        }
                        e.onNext(musicList)
                        e.onComplete()
                    })
                }
    }


    /**
     * 搜索音乐
     *
     * @param key
     * @param limit
     * @param page
     * @return
     */
    fun searchMusic(key: String, type: SearchEngine.Filter, limit: Int, page: Int): Observable<MutableList<Music>> {
        return Observable.create { result ->
            if (type == SearchEngine.Filter.ANY) {
                BaseApiImpl.searchSong(key, type.toString(), limit, page, success = {
                    val musicList = mutableListOf<Music>()
                    if (it.status) {
                        try {
                            val neteaseSize = it.data.netease.songs?.size ?: 0
                            val qqSize = it.data.netease.songs?.size ?: 0
                            val xiamiSize = it.data.netease.songs?.size ?: 0
                            val max = Math.max(Math.max(neteaseSize, qqSize), xiamiSize)
                            for (i in 0 until max) {
                                if (neteaseSize > i) {
                                    it.data.netease.songs?.get(i)?.let { music ->
                                        music.vendor = Constants.NETEASE
                                        musicList.add(MusicUtils.getMusic(music))
                                    }
                                }

                                if (qqSize > i) {
                                    it.data.qq.songs?.get(i)?.let { music ->
                                        music.vendor = Constants.QQ
                                        musicList.add(MusicUtils.getMusic(music))
                                    }
                                }

                                if (xiamiSize > i) {
                                    it.data.xiami.songs?.get(i)?.let { music ->
                                        music.vendor = Constants.XIAMI
                                        musicList.add(MusicUtils.getMusic(music))
                                    }
                                }
                            }
                        } catch (e: Throwable) {
                            LogUtil.e("search", e.message)
                        }
                        LogUtil.e("search", "结果：" + musicList.size)
                        result.onNext(musicList)
                        result.onComplete()
                    } else {
                        LogUtil.e("search", it.msg)
                        result.onError(Throwable(it.msg))
                    }
                }, fail = {
                    result.onError(Throwable(it
                            ?: SolidApplication.getInstance().getString(R.string.error_connection)))
                })
            } else {
                BaseApiImpl.searchSongSingle(key, type.toString(), limit, page, success = {
                    val musicList = mutableListOf<Music>()
                    if (it.status) {
                        try {
                            LogUtil.e("search type", type.toString().toLowerCase())
                            it.data.songs?.forEach { music ->
                                music.vendor = type.toString().toLowerCase()
                                musicList.add(MusicUtils.getMusic(music))
                            }
                        } catch (e: Throwable) {
                            LogUtil.e("search", e.message)
                        }
                        LogUtil.e("search", "结果：" + musicList.size)

                    } else {
                        LogUtil.e("search", it.msg)
//                        result.onError(Throwable(it.msg))
                    }
                    result.onNext(musicList)
                    result.onComplete()
                }, fail = {
                    LogUtil.e("search", it ?: "-0-")
//                    ToastUtils.show(it.toString())
//                    result.onNext(mutableListOf())
//                    result.onComplete()
//                    result.onError(Throwable(it
//                            ?: MusicApp.getAppContext().getString(R.string.error_connection)))
                })
            }
        }
    }

    /**
     * 获取热搜
     */
    fun getHotSearchInfo(): Observable<MutableList<HotSearchBean>> {
        return apiService.getHotSearchInfo()
                .flatMap { it ->
                    Observable.create(ObservableOnSubscribe<MutableList<HotSearchBean>> { e ->
                        try {
                            if (it.code == 200) {
                                val list = mutableListOf<HotSearchBean>()
                                it.result.hots?.forEach {
                                    list.add(HotSearchBean(it.first))
                                }
                                e.onNext(list)
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
}