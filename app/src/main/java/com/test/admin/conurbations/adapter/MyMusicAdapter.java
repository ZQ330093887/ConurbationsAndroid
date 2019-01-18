package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.model.entity.NewsList;
import com.test.admin.conurbations.utils.NavigationHelper;
import com.test.admin.conurbations.views.CircleImageView;

import javax.inject.Inject;

/**
 * Created by ZQiong on 2016/2/7 0007.
 */
public class MyMusicAdapter extends BaseListAdapter<NewsList> {
    private Activity mContext;

    @Inject
    public MyMusicAdapter(Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final NewsList playlist) {

        vh.setText(R.id.tv_name, playlist.name)
                .setText(R.id.tv_num, playlist.total + "首")
                .setVisibility(R.id.tv_num, playlist.total == 0 ? View.GONE : View.VISIBLE);

        CircleImageView view = vh.getView(R.id.iv_cover);
//        view.setBorderWidth(1);
//                .setBorderColor(Color.RED)
//                .setType(RoundImageView.TYPE_ROUND)
//                .setLeftTopCornerRadius(0)
//                .setRightTopCornerRadius(10)
//                .setRightBottomCornerRadius(30)
//                .setLeftBottomCornerRadius(50);

        vh.setImageUrlUserGlide(view, playlist.coverUrl, R.mipmap.default_cover);
        vh.itemView.setOnClickListener(v -> {
            //
            NavigationHelper.navigateToPlaylist(mContext, playlist, null);
        });
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new MvMusicHolder(inflateItemView(parent, R.layout.item_playlist));
    }

    class MvMusicHolder extends BaseViewHolder {
        public MvMusicHolder(View itemView) {
            super(itemView);
        }
    }
}
