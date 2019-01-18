package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaseActivity;
import com.test.admin.conurbations.activitys.MvDetailActivity;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.entity.MvInfo;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.utils.MusicUtils;
import com.test.admin.conurbations.utils.NavigationHelper;

/**
 * Created by ZQiong on 2016/2/7 0007.
 */
public class TopPlaylistAdapter extends BaseListAdapter<NewsList> {
    private Activity context;

    public TopPlaylistAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final NewsList playlist) {

        String url = playlist.coverUrl;
        if (playlist.type.equals(Constants.PLAYLIST_WY_ID)) {
            url = MusicUtils.getAlbumPic(playlist.coverUrl, Constants.NETEASE, 90);
        }

        vh.setText(R.id.tv_title, playlist.name)
                .setText(R.id.tv_playCount, "播放次数：" + playlist.playCount)
                .setImageUrlUserGlide(vh.getView(R.id.iv_cover), url, R.color.white);
        vh.itemView.setTag(playlist);
        vh.itemView.setOnClickListener(this::onClickListener);
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new TopPlaylistHolder(inflateItemView(parent, R.layout.item_play_list));
    }

    private class TopPlaylistHolder extends BaseViewHolder {
        private TopPlaylistHolder(View itemView) {
            super(itemView);
        }
    }

    private void onClickListener(View view) {
        NewsList playlist = (NewsList) view.getTag();
        Pair pair = new Pair(view.findViewById(R.id.iv_cover),
                context.getResources().getString(R.string.transition_album));
        NavigationHelper.navigateToPlaylist(context, playlist, pair);
    }
}
