package com.test.admin.conurbations.utils.download;

import android.text.TextUtils;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.db.DaoLitepal;
import com.test.admin.conurbations.utils.FileUtils;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.widget.SolidApplication;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class DownloadLoader {
    private String TAG = "PlayQueueLoader";

    /**
     * 获取已下载列表
     */
    public static List<Music> getDownloadList(boolean isCached) {
        List<Music> musicList = new ArrayList<>();

        List<TasksManagerModel> data = LitePal.where("finish = 1 and cache = ?", isCached ? "1" : "0").find(TasksManagerModel.class);

        for (TasksManagerModel it : data) {
            Music music = null;
            if (!TextUtils.isEmpty(it.mid)) {
                music = DaoLitepal.getMusicInfo(it.mid);
            }

//            if (music != null) {
//                for (Music origin : music) {
//                    if (origin.uri != null || origin.uri.startsWith("http:")) {
//                        origin.uri = it.path;
//                        origin.isOnline = false;
//                    }
//                    SongLoader.updateMusic(origin);
//                }
//            }
            if (music != null) {
                musicList.add(music);
            }
        }

        return musicList;
    }

    /**
     * 获取已下载列表
     */
    public static List<Music> getDownloadList() {
        List<Music> musicList = new ArrayList<>();

        List<TasksManagerModel> data = LitePal.where("finish = 1").find(TasksManagerModel.class);

        for (TasksManagerModel it : data) {
            Music music = null;
            if (!TextUtils.isEmpty(it.mid)) {
                music = DaoLitepal.getMusicInfo(it.mid);
            }

//            if (music != null) {
//                for (Music origin : music) {
//                    if (origin.uri != null || origin.uri.startsWith("http:")) {
//                        origin.uri = it.path;
//                        origin.isOnline = false;
//                    }
//                    SongLoader.updateMusic(origin);
//                }
//            }
            if (music != null) {
                musicList.add(music);
            }
        }

        return musicList;
    }

    /**
     * 获取已下载列表
     */
    public static List<TasksManagerModel> getDownloadingList() {
        return LitePal.where("finish = 0").find(TasksManagerModel.class);
    }

    /**
     * 获取已下载列表
     */
    public List<TasksManagerModel> getAllDownloadList() {
        return LitePal.findAll(TasksManagerModel.class);
    }

    /**
     * 是否已在下载列表
     */
    public static Boolean isHasMusic(String mid) {
        return LitePal.isExist(TasksManagerModel.class, "mid = ?", mid);
    }

    public static TasksManagerModel addTask(int tid, String mid, String name, String url, String path, Boolean cached) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        //判断是否已下载过
        if (!cached && isHasMusic(mid)) {
            ToastUtils.getInstance().showToast(SolidApplication.getInstance().getString(R.string.download_exits, name));
            return null;
        }
        int id = FileDownloadUtils.generateId(url, path);
        TasksManagerModel model = new TasksManagerModel();
        model.tid = id;
        model.mid = mid;
        model.name = name;
        model.url = url;
        model.path = path;
        model.finish = false;
        model.cache = cached;
        model.saveOrUpdate("tid = ?", String.valueOf(tid));
        return model;
    }

    /**
     * 更新数据库下载任务状态
     */
    public static void updateTask(int tid) {
        TasksManagerModel model = LitePal.where("tid = ?", String.valueOf(tid)).find(TasksManagerModel.class).get(0);
        Music music = null;
        if (!TextUtils.isEmpty(model.mid)) {
            music = DaoLitepal.getMusicInfo(model.mid);
        }

        model.finish = true;
        model.saveOrUpdate("tid = ?", String.valueOf(tid));
        //更新mp3文件标签
        if (music != null) {
            LogUtil.e(model.path);
//            Mp3Util.updateTagInfo(model.path, music);
//            Mp3Util.getTagInfo(model.path);
        }
        NavigationHelper.scanFileAsync(SolidApplication.getInstance(), FileUtils.getMusicDir());
    }
}
