package com.test.admin.conurbations.activitys;

import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.utils.download.TasksManagerModel;

import java.util.List;

/**
 * Created by zhouqiong on 2016/12/5.
 */

public interface IDownLoadView extends BaseViewImpl {

    void showSongs(List<Music> musicList);

    void showDownloadList(List<TasksManagerModel> modelList);
}
