package com.test.admin.conurbations.presenter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.test.admin.conurbations.activitys.IVideoLiveView;
import com.test.admin.conurbations.activitys.WebViewActivity;
import com.test.admin.conurbations.model.entity.VideoLiveSource;
import com.test.admin.conurbations.model.response.VideoLiveData;
import com.test.admin.conurbations.model.response.VideoLiveSourceData;
import com.test.admin.conurbations.utils.TmiaaoUtils;

import java.util.List;

/**
 * Created by zhouqiong on 2017/5/18.
 */
public class MatchVideoLivePresenter extends BasePresenter {

    private IVideoLiveView videoLiveView;
    private Context mContext;

    public MatchVideoLivePresenter(IVideoLiveView videoLiveView, Context mContext) {
        this.videoLiveView = videoLiveView;
        this.mContext = mContext;
    }

    public void getVideoLiveInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final VideoLiveData videoLiveData = TmiaaoUtils.getLiveList();
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (videoLiveData != null && videoLiveData.items.size() > 0) {
                            videoLiveView.setVideoLiveData(videoLiveData);
                        }
                    }
                });
            }

        }).start();
    }

    public static void getVideoLiveSourceInfo(final String link, final Context mContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final VideoLiveSourceData videoLiveData = TmiaaoUtils.getSourceList(link);
                final List<VideoLiveSource> list = videoLiveData.items;
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list != null && list.size() == 1) {
                            WebViewActivity.openUrl(mContext, list.get(0).link, list.get(0).name, false, false);
                            return;
                        } else if (list == null || list.isEmpty()) {
                            return;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        final String[] links = new String[list.size()];
                        final String[] names = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            links[i] = list.get(i).link;
                            names[i] = list.get(i).name;
                        }

                        builder.setTitle("请选择直播源")
                                .setItems(names, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (links[which].startsWith("/")) {
                                            links[which] = "http://nba.tmiaoo.com" + links[which];
                                        }
                                        WebViewActivity.openUrl(mContext, links[which], names[which], false, false);
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
        }).start();
    }

}
