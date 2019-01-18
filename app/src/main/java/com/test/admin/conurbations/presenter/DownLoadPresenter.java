package com.test.admin.conurbations.presenter;

import android.app.Activity;

import com.test.admin.conurbations.activitys.IDownLoadView;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.utils.download.DownloadLoader;
import com.test.admin.conurbations.utils.download.TasksManagerModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class DownLoadPresenter extends BasePresenter<IDownLoadView> {


    @Inject
    public DownLoadPresenter() {
    }

    public void loadDownloadMusic(boolean isCache, Activity activity) {
        new Thread(() -> {
            List<Music> data = DownloadLoader.getDownloadList(isCache);
            activity.runOnUiThread(() -> {
                mvpView.showSongs(data);
                mvpView.showFinishState();
            });
        }).start();
    }

    public void loadDownloading(Activity activity) {
        new Thread(() -> {
            List<TasksManagerModel> data = DownloadLoader.getDownloadingList();
            activity.runOnUiThread(() -> {
                mvpView.showDownloadList(data);
                mvpView.showFinishState();
            });
        }).start();
    }
}
