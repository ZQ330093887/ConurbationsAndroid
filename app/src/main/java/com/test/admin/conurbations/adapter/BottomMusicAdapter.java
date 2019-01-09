package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.api.MusicApi;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.NavigationHelper;

/**
 * Created by zhouqiong on 2017/1/5.
 */

public class BottomMusicAdapter extends BaseListAdapter<Music> {

    private Activity activity;

    public ImageView coverImageView;

    public BottomMusicAdapter(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final Music item) {

        holder.setText(R.id.tv_title, CommonUtil.getTitle(item.title))
                .setText(R.id.tv_artist, CommonUtil.getArtistAndAlbum(item.artist, item.album));

        coverImageView = holder.getView(R.id.iv_cover);
        if (item.coverUri != null) {
            holder.setImageUrlUserGlide(coverImageView, item.coverUri, R.mipmap.default_cover);
        } else {
            //加载歌曲专辑图
            if (!TextUtils.isEmpty(item.title)) {
                ApiManager.request(MusicApi.INSTANCE.getDoubanPic(item.title), new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        item.coverUri = result;
                        holder.setImageUrlUserGlide(coverImageView, item.coverUri, R.mipmap.default_cover);
                    }

                    @Override
                    public void error(String msg) {
                    }
                });
            }
        }
        holder.itemView.setOnClickListener(v -> {
            //
            NavigationHelper.navigateToPlaying(activity, coverImageView);
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new BottomMusicViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_bottom_music));
    }

    class BottomMusicViewHolder extends BaseViewHolder {
        public BottomMusicViewHolder(ViewGroup parent) {
            super(parent);
        }
    }
}
