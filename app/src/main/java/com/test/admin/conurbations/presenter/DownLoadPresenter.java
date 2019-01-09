package com.test.admin.conurbations.presenter;

import android.app.Activity;

import com.test.admin.conurbations.activitys.IDownLoadView;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.download.DownloadLoader;
import com.test.admin.conurbations.utils.download.TasksManagerModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class DownLoadPresenter extends BasePresenter<IDownLoadView> {

    private boolean isCache;
    private Activity activity;

    @Inject
    public DownLoadPresenter() {
        Disposable subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.DOWNLOAD_EVENT)) {
                loadDownloadMusic(isCache, activity);
                loadDownloading(activity);
            }
        });
    }


    public void loadDownloadMusic(boolean isCache, Activity activity) {
        this.isCache = isCache;
        this.activity = activity;
        new Thread(() -> {
            List<Music> data = DownloadLoader.getDownloadList(isCache);
            activity.runOnUiThread(() -> {
                mvpView.showSongs(data);
                mvpView.showFinishState();
            });
        }).start();
    }

    public void loadDownloading(Activity activity) {
        this.activity = activity;
        new Thread(() -> {
            List<TasksManagerModel> data = DownloadLoader.getDownloadingList();
            activity.runOnUiThread(() -> {
                mvpView.showDownloadList(data);
                mvpView.showFinishState();
            });
        }).start();

    }
}
