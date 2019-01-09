package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.BaiduMusicListActivity;
import com.test.admin.conurbations.activitys.NeteasePlayListActivity;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.entity.NewsList;

import javax.inject.Inject;

/**
 * OnlineAdapter
 * Created by zhouqiong on 2017/1/5.
 */

public class NetPlayListAdapter extends BaseListAdapter<NewsList> {

    private int[] viewIds = {R.id.tv_music_1, R.id.tv_music_2, R.id.tv_music_3};
    private int[] stringIds = {R.string.song_list_item_title_1, R.string.song_list_item_title_2, R.string.song_list_item_title_3};
    private Context context;

    @Inject
    public NetPlayListAdapter(Fragment context) {
        super(context);
        this.context = context.getContext();
    }

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final NewsList playlist) {
        holder.setImageUrlUserGlide(holder.getView(R.id.iv_cover), playlist.coverUrl, R.color.white)
                .setText(R.id.title, playlist.name);

        holder.setOnClickListener(R.id.item_net_play,
                getListener(playlist, holder.getView(R.id.item_net_play)));

        for (int i = 0; i < viewIds.length; i++) {
            if (playlist.musicList.size() <= i) {
                continue;
            }
            Music music = playlist.musicList.get(i);
            holder.setText(viewIds[i], context.getString(stringIds[i], music.title, music.artist));
        }
    }

    @NonNull
    private View.OnClickListener getListener(final NewsList list, final View view) {
        return v -> {
            Context context = view.getContext();
            Intent intent = new Intent();
            switch (list.type) {
                case Constants.PLAYLIST_BD_ID:
                    intent.setClass(context, BaiduMusicListActivity.class);
                    intent.putExtra(BaiduMusicListActivity.PLAY_LIST, list);
                    break;
                case Constants.PLAYLIST_WY_ID:
                    intent.setClass(context, NeteasePlayListActivity.class);
                    intent.putExtra(NeteasePlayListActivity.PLAY_LIST, list);
                    break;
                case Constants.PLAYLIST_QQ_ID:
                    intent.setClass(context, NeteasePlayListActivity.class);
                    intent.putExtra(NeteasePlayListActivity.PLAY_LIST, list);
                    break;
            }

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                    ((Activity) context, new Pair<>(view, list.name));
            ActivityCompat.startActivity(context, intent, activityOptionsCompat.toBundle());
        };
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new NetPlayListAdapter.SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_online_large));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(ViewGroup parent) {
            super(parent);
        }
    }
}
