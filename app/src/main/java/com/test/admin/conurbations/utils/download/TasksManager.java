package com.test.admin.conurbations.utils.download;

import android.text.TextUtils;
import android.util.SparseArray;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.test.admin.conurbations.adapter.TaskItemAdapter;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.fragments.DownloadManagerFragment;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.widget.SolidApplication;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by ZQiong on 2018/12/7.
 */
public class TasksManager {


    private static TasksManager instance = null;

    private TasksManager() {
    }

    public static TasksManager getInstance() {
        if (instance == null) {
            synchronized (TasksManager.class) {
                if (instance == null) {
                    instance = new TasksManager();
                }
            }
        }
        return instance;
    }


    private static List<TasksManagerModel> modelList = DownloadLoader.getDownloadingList();

    private static SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    private static FileDownloadConnectListener listener;


    public void addTaskForViewHolder(BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(int id) {
        taskSparseArray.remove(id);
    }

    public void updateViewHolder(int id, TaskItemAdapter.TaskItemViewHolder holder) {
        if (taskSparseArray.get(id) != null) {
            taskSparseArray.get(id).setTag(holder);
        }
    }

    public static void releaseTask() {
        taskSparseArray.clear();
    }

    /**
     * 注册监听
     */
    private static void registerServiceConnectionListener(WeakReference<DownloadManagerFragment> activityWeakReference) {
        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }

        listener = new FileDownloadConnectListener() {
            @Override
            public void connected() {
                if (activityWeakReference.get() == null) {
                    return;
                }
                activityWeakReference.get().postNotifyDataChanged();
            }

            @Override
            public void disconnected() {
                if (activityWeakReference.get() == null) {
                    return;
                }
                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    private static void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public static void onCreate(WeakReference<DownloadManagerFragment> activityWeakReference) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(activityWeakReference);
        } else {
            registerServiceConnectionListener(activityWeakReference);
        }
    }

    public static void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTask();
    }

    /**
     * 根据位置获取
     */


    public TasksManagerModel get(int position) {
        return modelList.get(position);
    }

    /**
     * 根据model id获取对象
     */
    private static TasksManagerModel getById(int id) {
        for (TasksManagerModel model : modelList) {
            if (model.tid == id) {
                return model;
            }
        }
        return null;
    }

    public Boolean isDownloaded(int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }


    public List<TasksManagerModel> getModelList() {
        return modelList;
    }


    /**
     * @param tid :下载任务唯一ID
     */
    public void finishTask(int tid) {
        DownloadLoader.updateTask(tid);

        modelList = DownloadLoader.getDownloadingList();
        RxBus.getDefault().post(new Event(false, Constants.DOWNLOAD_EVENT));
    }

    /**
     * @param tid :下载任务唯一ID
     */
    public static TasksManagerModel addTask(int tid, String mid, String name, String url, String path, boolean isCached) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(mid) || TextUtils.isEmpty(path)) {
            return null;
        }

        TasksManagerModel model = getById(tid);
        if (model != null) {
            return model;
        }
        TasksManagerModel newModel = DownloadLoader.addTask(tid, mid, name, url, path, isCached);
        if (newModel != null) {
            modelList.add(newModel);
        }
        return newModel;
    }

}
