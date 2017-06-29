package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.IVideoLiveSourceView;
import com.test.admin.conurbations.activitys.WebViewActivity;
import com.test.admin.conurbations.model.entity.VideoLiveInfo;
import com.test.admin.conurbations.model.entity.VideoLiveSource;
import com.test.admin.conurbations.presenter.MatchVideoLivePresenter;

import java.util.List;

public class MatchVideoLiveListAdapter extends BaseListAdapter<VideoLiveInfo> implements IVideoLiveSourceView {

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final VideoLiveInfo info) {

        vh.setImageUrlUserGlide(R.id.iv_left_leam, info.leftImg, 0.918f, R.mipmap.nba_default)
                .setImageUrlUserGlide(R.id.iv_right_team, info.rightImg, 0.918f, R.mipmap.nba_default)
                .setText(R.id.tv_left_leam, info.leftName)
                .setText(R.id.tv_right_team, info.rightName)
                .setText(R.id.tv_live_time, info.time)
                .setText(R.id.tv_live_type, info.type)
                .setOnClickListener(R.id.rl_match_video_live, v -> getMatchVideoLivePresenter().getVideoLiveSourceInfo(info.link, v.getContext()))
        ;
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new MatchVideoLiveHolder(inflateItemView(parent, R.layout.item_match_video_list));
    }

    class MatchVideoLiveHolder extends BaseViewHolder {
        public MatchVideoLiveHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private MatchVideoLivePresenter getMatchVideoLivePresenter() {
        return new MatchVideoLivePresenter(this);
    }

    @Override
    public void setVideoLiveData(List<VideoLiveSource> videoLiveData, Context mContext) {
        setVideoLiveView(videoLiveData, mContext);
    }

    private static void setVideoLiveView(List<VideoLiveSource> list, final Context mContext) {
        if (list != null && list.size() == 1) {
            WebViewActivity.openUrl(mContext, list.get(0).link, list.get(0).name, false, false);
            return;
        } else if (list == null || list.isEmpty()) {
            return;
        }

        getAlertDialogAndSetData(list, mContext);
    }

    private static void getAlertDialogAndSetData(List<VideoLiveSource> list, final Context mContext) {

        final String[] links = new String[list.size()];
        final String[] names = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            links[i] = list.get(i).link;
            names[i] = list.get(i).name;
        }

        new AlertDialog.Builder(mContext)
                .setTitle("请选择直播源")
                .setItems(names, (dialog, which) -> {
                    if (links[which].startsWith("/")) {
                        links[which] = "http://nba.tmiaoo.com" + links[which];
                    }
                    WebViewActivity.openUrl(mContext, links[which], names[which], false, false);
                    dialog.dismiss();
                }).show();
    }
}
