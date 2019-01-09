package com.test.admin.conurbations.utils.download;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.db.DaoLitepal;
import com.test.admin.conurbations.utils.SaveBitmapUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class SongLoader {

    /**
     * 本地歌曲
     * 添加歌曲
     */
    public static void updateMusic(Music music) {
        DaoLitepal.saveOrUpdateMusic(music, false);
    }

    /**
     * 获取艺术家所有歌曲
     *
     * @return
     */
    public static List<Music> getSongsForArtist(String artistName) {
        return LitePal.where("isonline =0 and artist like ?", "%$artistName%").find(Music.class);
    }

    /**
     * 删除歌曲
     */
    public static void removeSong(Music music) {
        DaoLitepal.deleteMusic(music);
    }

    /**
     * 获取所有收藏的歌曲
     *
     * @return
     */
    public static List<Music> getFavoriteSong() {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_LOVE_ID, "");
    }

    /**
     * 获取专辑所有歌曲
     *
     * @return
     */
    public static List<Music> getSongsForAlbum(String albumName) {
        return LitePal.where("isonline =0 and album like ?", "%$albumName%").find(Music.class);
    }

    public static boolean updateFavoriteSong(Music music) {
        music.isLove = !music.isLove;
        DaoLitepal.saveOrUpdateMusic(music, false);
        return music.isLove;
    }

    public static List<Music> getLocalMusic(Context context, boolean isReload) {
        List<Music> data = getSongsForDB();
        if (data.size() == 0 || isReload) {
            data.clear();
            List<Music> musicLists = getAllLocalSongs(context);
            if (isReload) {
                DaoLitepal.updateAlbumList();
                DaoLitepal.updateArtistList();
            }
            data.addAll(musicLists);
        }
        return data;
    }

    public static List<Music> getSongsForDB() {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_LOCAL_ID, "");
    }

    public static List<Music> getAllLocalSongs(Context context) {
        return getSongsForMedia(context, makeSongCursor(context, null, null));
    }

    /**
     * Android 扫描获取到的数据
     *
     * @param context
     * @param cursor
     * @return
     */
    private static List<Music> getSongsForMedia(Context context, Cursor cursor) {
        List<Music> results = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int is_music = cursor.getInt(9);
                    long id = cursor.getLong(0);
                    String title = cursor.getString(1);
                    String artist = cursor.getString(2);
                    String album = cursor.getString(3);
                    int duration = cursor.getInt(4);
                    int trackNumber = cursor.getInt(5);
                    String artistId = cursor.getString(6);
                    String albumId = cursor.getString(7);
                    String path = cursor.getString(8);
                    String coverUri = SaveBitmapUtils.getCoverUri(context, albumId);
                    Music music = new Music();
                    music.type = Constants.LOCAL;
                    music.isOnline = false;
                    music.mid = String.valueOf(id);
                    music.album = album;
                    music.albumId = albumId;
                    music.artist = artist.equals("<unknown>") ? "未知歌手" : artist;
                    music.artistId = artistId;
                    music.uri = path;
                    if (coverUri != null) {
                        music.coverUri = coverUri;
                    }
                    music.trackNumber = trackNumber;
                    music.duration = duration;
                    music.title = title;
                    music.date = System.currentTimeMillis();
                    DaoLitepal.saveOrUpdateMusic(music, false);
                    results.add(music);
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
        String songSortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        return makeSongCursor(context, selection, paramArrayOfString, songSortOrder);
    }

    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "duration>60000 AND is_music=1 AND title != ''";

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = "$selectionStatement AND $selection";
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", MediaStore.Audio.Media.DATA, "is_music"},
                selectionStatement, paramArrayOfString, sortOrder);
    }

}
