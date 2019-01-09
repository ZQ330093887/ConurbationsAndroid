package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.utils.CommonUtil;

/**
 * Created by ZQiong on 2018/11/30
 */
public class QueueAdapter extends BaseListAdapter<Music> {


    public QueueAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final Music item) {

        vh.setText(R.id.tv_title, CommonUtil.getTitle(item.title))
                .setText(R.id.tv_artist, CommonUtil.getArtistAndAlbum(item.artist, item.album))
                .setOnClickListener(R.id.iv_more, getListener(vh.getView(R.id.iv_more), pos))
                .setOnClickListener(R.id.container, getListener(vh.getView(R.id.container), pos));


        if (PlayManager.getPlayingId().equals(item.mid)) {
            vh.setTextColor(R.id.tv_title, Color.parseColor("#0091EA"))
                    .setTextColor(R.id.tv_artist, Color.parseColor("#01579B"));
        } else {
            vh.setTextColor(R.id.tv_title, Color.parseColor("#000000"))
                    .setTextColor(R.id.tv_artist, Color.parseColor("#9e9e9e"));
        }
        if (item.type.equals(Constants.LOCAL)) {
            vh.setVisibility(R.id.iv_resource, View.GONE);
        } else {
            vh.setVisibility(R.id.iv_resource, View.VISIBLE);
            switch (item.type) {
                case Constants.BAIDU:
                    vh.setImageSource(R.id.iv_resource, R.mipmap.baidu);
                    break;
                case Constants.NETEASE:
                    vh.setImageSource(R.id.iv_resource, R.mipmap.netease);
                    break;
                case Constants.QQ:
                    vh.setImageSource(R.id.iv_resource, R.mipmap.qq);
                    break;
                case Constants.XIAMI:
                    vh.setImageSource(R.id.iv_resource, R.mipmap.xiami);
                    break;
            }
        }

    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new QueueDialogHolder(inflateItemView(parent, R.layout.item_queue));
    }

    class QueueDialogHolder extends BaseViewHolder {
        public QueueDialogHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    private View.OnClickListener getListener(View view, int pos) {
        return v -> mListener.onItemClick(view, pos);
    }


    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
