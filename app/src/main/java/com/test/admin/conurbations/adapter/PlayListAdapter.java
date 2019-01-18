package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.Artist;
import com.test.admin.conurbations.utils.MusicUtils;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.views.CircleImageView;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2016/2/7 0007.
 */
public class PlayListAdapter extends BaseListAdapter<Artist> {
    private Activity mContext;

    @Inject
    public PlayListAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final Artist artist) {

        vh.setText(R.id.tv_name, artist.name);


        CircleImageView view = vh.getView(R.id.iv_cover);
//        view.setBorderWidth(1);
        vh.setImageUrlUserGlide(view, MusicUtils.getAlbumPic(artist.picUrl, artist.type, 90), R.mipmap.default_cover);

        vh.itemView.setOnClickListener(v -> {
            //
            NavigationHelper.navigateToPlaylist(mContext, artist, new Pair<>(vh.getView(R.id.iv_cover), mContext.getResources().getString(R.string.transition_album)));
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new PlayListHolder(inflateItemView(parent, R.layout.item_playlist));
    }

    class PlayListHolder extends BaseViewHolder {
        public PlayListHolder(View itemView) {
            super(itemView);
        }
    }
}
