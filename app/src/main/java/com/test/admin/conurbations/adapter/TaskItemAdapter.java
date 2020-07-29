package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.utils.LogUtil;
import com.test.admin.conurbations.utils.download.TasksManager;
import com.test.admin.conurbations.utils.download.TasksManagerModel;

import java.io.File;
import java.util.List;

/**
 * Created by ZQiong on 2018/12/7.
 */
public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.TaskItemViewHolder> {
    private static final String TAG = "TaskItemAdapter";
    private Context mContext;
    private List<TasksManagerModel> models;


    public List<TasksManagerModel> getModels() {
        return models;
    }

    public void setModels(List<TasksManagerModel> models) {
        this.models = models;
    }

    public TaskItemAdapter(Context mContext, List<TasksManagerModel> list) {
        this.mContext = mContext;
        this.models = list;
    }

    public static final FileDownloadListener taskDownloadListener = new FileDownloadListener();

    private View.OnClickListener taskActionOnClickListener = v -> {
        if (v.getTag() == null) {
            return;
        }

        TaskItemViewHolder holder = (TaskItemViewHolder) v.getTag();

        CharSequence action = ((TextView) v).getText();
        if (action.equals(v.getResources().getString(R.string.pause))) {
            // to pause
            FileDownloader.getImpl().pause(holder.id);
        } else if (action.equals(v.getResources().getString(R.string.start))) {
            // to start
            // to start
            final TasksManagerModel model = TasksManager.getInstance().get(holder.position);
            final BaseDownloadTask task = FileDownloader.getImpl().create(model.url)
                    .setPath(model.path)
                    .setCallbackProgressTimes(100)
                    .setListener(taskDownloadListener);
            TasksManager.getInstance().addTaskForViewHolder(task);
            holder.id = task.getId();
            TasksManager.getInstance().updateViewHolder(holder.id, holder);

            task.start();
        } else if (action.equals(v.getResources().getString(R.string.delete))) {
            // to delete
            new File(TasksManager.getInstance().get(holder.position).path).delete();
            holder.taskActionBtn.setEnabled(true);
            holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS, 0, 0);
        }
    };

    @NonNull
    @Override
    public TaskItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_download_music, parent, false);
        return new TaskItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskItemViewHolder holder, int position) {
        TasksManagerModel model = models.get(position);
        holder.taskActionBtn.setOnClickListener(taskActionOnClickListener);
        holder.update(model.tid, holder.getAdapterPosition());
        holder.taskActionBtn.setTag(holder);
        holder.taskNameTv.setText(model.name);

        TasksManager.getInstance().updateViewHolder(holder.id, holder);

        holder.taskActionBtn.setEnabled(true);
        if (FileDownloader.getImpl().isServiceConnected()) {
            final int status = TasksManager.getInstance().getStatus(model.tid, model.path);
            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                    status == FileDownloadStatus.connected) {
                // start task, but file not created yet
                holder.updateDownloading(status, TasksManager.getInstance().getSoFar(model.tid)
                        , TasksManager.getInstance().getTotal(model.tid));
            } else if (!new File(model.path).exists() &&
                    !new File(FileDownloadUtils.getTempPath(model.path)).exists()) {
                // not exist file
                holder.updateNotDownloaded(status, 0, 0);
            } else if (TasksManager.getInstance().isDownloaded(status)) {
                // already downloaded and exist
                LogUtil.e(TAG, "already downloaded and exist");
                holder.updateDownloaded();
            } else if (status == FileDownloadStatus.progress) {
                // downloading
                holder.updateDownloading(status, TasksManager.getInstance().getSoFar(model.tid)
                        , TasksManager.getInstance().getTotal(model.tid));
            } else {
                // not start
                holder.updateNotDownloaded(status, TasksManager.getInstance().getSoFar(model.tid)
                        , TasksManager.getInstance().getTotal(model.tid));
            }
        } else {
            holder.taskStatusTv.setText(R.string.tasks_manager_demo_status_loading);
            holder.taskActionBtn.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class TaskItemViewHolder extends BaseViewHolder {
        public TaskItemViewHolder(View itemView) {
            super(itemView);
            assignViews();
        }

        private View findViewById(final int id) {
            return itemView.findViewById(id);
        }

        /**
         * viewHolder position
         */
        private int position;
        /**
         * download id
         */
        public int id;

        public void update(final int id, final int position) {
            this.id = id;
            this.position = position;
        }

        public void updateDownloaded() {
            taskPb.setMax(1);
            taskPb.setProgress(1);
            taskStatusTv.setText(R.string.tasks_manager_demo_status_completed);
            taskActionBtn.setText(R.string.delete);
            taskActionBtn.setVisibility(View.GONE);
            taskPb.setVisibility(View.GONE);
            TasksManager.getInstance().finishTask(id);
            // TODO: 2018/12/7 处理Bus
//            EventBus.getDefault().post(new DownloadEvent());
        }

        public void updateNotDownloaded(final int status, final long sofar, final long total) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar
                        / (float) total;
                taskPb.setMax(100);
                taskPb.setProgress((int) (percent * 100));
            } else {
                taskPb.setMax(1);
                taskPb.setProgress(0);
            }

            switch (status) {
                case FileDownloadStatus.error:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_error);
                    break;
                case FileDownloadStatus.paused:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
                    break;
                default:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_not_downloaded);
                    break;
            }
            taskActionBtn.setText(R.string.start);
        }

        public void updateDownloading(final int status, final long sofar, final long total) {
            final float percent = sofar
                    / (float) total;
            taskPb.setMax(100);
            taskPb.setProgress((int) (percent * 100));

            switch (status) {
                case FileDownloadStatus.pending:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
                    break;
                case FileDownloadStatus.started:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
                    break;
                case FileDownloadStatus.connected:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
                    break;
//                case FileDownloadStatus.progress:
//                    taskStatusTv.setText(R.string.tasks_manager_demo_status_progress);
//                    break;
                default:
                    taskStatusTv.setText(mContext.getString(
                            R.string.tasks_manager_demo_status_downloading, (int) (percent * 100)));
                    break;
            }

            taskActionBtn.setText(R.string.pause);
        }

        TextView taskNameTv;
        TextView taskStatusTv;
        ProgressBar taskPb;
        Button taskActionBtn;

        private void assignViews() {
            taskNameTv = (TextView) findViewById(R.id.task_name_tv);
            taskStatusTv = (TextView) findViewById(R.id.task_status_tv);
            taskPb = (ProgressBar) findViewById(R.id.task_pb);
            taskActionBtn = (Button) findViewById(R.id.task_action_btn);
        }
    }
}
