package com.test.admin.conurbations.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.admin.conurbations.R;
import com.test.admin.conurbations.config.Constants;
import com.test.admin.conurbations.model.Music;
import com.test.admin.conurbations.model.api.BaiduApiService;
import com.test.admin.conurbations.model.entity.DoubanMusic;
import com.test.admin.conurbations.player.PlayManager;
import com.test.admin.conurbations.retrofit.ApiManager;
import com.test.admin.conurbations.retrofit.RequestCallBack;
import com.test.admin.conurbations.rxbus.Event;
import com.test.admin.conurbations.rxbus.RxBus;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.DisplayUtils;
import com.test.admin.conurbations.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by ZQiong on 2018/11/30
 */
public class SongAdapter extends BaseListAdapter<Music> {

    private Activity activity;

    @Inject
    public SongAdapter(Activity context) {
        super(context);
        this.activity = context;

        Disposable subscribe = RxBus.getDefault().toObservable(Event.class).subscribe(event -> {
            if (event.eventType.equals(Constants.META_CHANGED_EVENT)) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void bindDataToItemView(BaseViewHolder vh, final Music item) {
        vh.setImageUrlUserGlide(vh.getView(R.id.iv_cover), item.coverUri, R.color.white)
                .setText(R.id.tv_title, CommonUtil.getTitle(item.title))
                .setText(R.id.tv_artist, CommonUtil.getArtistAndAlbum(item.artist, item.album));

        if (PlayManager.getPlayingId().equals(item.mid)) {
            vh.getView(R.id.v_playing).setVisibility(View.VISIBLE);

        } else {
            vh.getView(R.id.v_playing).setVisibility(View.GONE);
        }
        vh.setOnClickListener(R.id.iv_more, getListener(item, vh.getView(R.id.iv_more), pos))
                .setOnClickListener(R.id.container, getListener(item, vh.getView(R.id.container), pos));

        //设置播放状态
        if (PlayManager.getPlayingId().equals(item.mid)) {
            vh.setVisibility(R.id.v_playing, View.VISIBLE)
                    .setTextColor(R.id.tv_title, ContextCompat.getColor(activity, R.color.app_green))
                    .setTextColor(R.id.tv_artist, ContextCompat.getColor(activity, R.color.app_green));

//            recyclerView.scrollToPosition(vh.getAdapterPosition());
        } else {
            vh.setVisibility(R.id.v_playing, View.GONE)
                    .setTextColor(R.id.tv_title, ContextCompat.getColor(activity, R.color.black))
                    .setTextColor(R.id.tv_artist, ContextCompat.getColor(activity, R.color.gray));
        }
        Drawable quality;
        if (item.sq) {
            quality = activity.getResources().getDrawable(R.mipmap.sq_icon, null);
        } else if (item.hq) {
            quality = activity.getResources().getDrawable(R.mipmap.hq_icon, null);
        } else {
            quality = null;
        }

        if (quality != null) {
            quality.setBounds(0, 0, (int) (quality.getMinimumWidth() + DisplayUtils.px2dp(2, activity)), quality.getMinimumHeight());
            ((TextView) vh.getView(R.id.tv_artist)).setCompoundDrawables(quality, null, null, null);
        }

        if (item.type.equals(Constants.LOCAL)) {
            vh.getView(R.id.iv_resource).setVisibility(View.GONE);
        } else {
            vh.getView(R.id.iv_resource).setVisibility(View.VISIBLE);
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

        if (item.coverUri != null) {
            vh.setImageUrlUserGlide(vh.getView(R.id.iv_cover), item.coverUri, R.color.white);
        }
        if (TextUtils.isEmpty(item.coverUri) && !TextUtils.isEmpty(item.title)) {
            //加载歌曲专辑图
            Map<String, String> params = new HashMap<>();
            params.put("q", item.title);
            params.put("count", "10");
            Observable<DoubanMusic> search = ApiManager.getInstance().create(BaiduApiService.class, "https://douban.uieee.com/")
                    .searchMusic("search", params);
            ApiManager.request(search,
                    new RequestCallBack<DoubanMusic>() {
                        @Override
                        public void success(DoubanMusic result) {
                            item.coverUri = result.musics.get(0).image;
                            vh.setImageUrlUserGlide(vh.getView(R.id.iv_cover), item.coverUri, R.color.white);
                        }

                        @Override
                        public void error(String msg) {
                            ToastUtils.getInstance().showToast(msg);
                            System.out.println("***********" + msg);
                        }
                    });
        }
        if (item.isCp) {
            vh.getView(R.id.isCpView).setVisibility(View.VISIBLE);
            vh.setTextColor(R.id.tv_title, ContextCompat.getColor(activity, R.color.gray))
                    .setTextColor(R.id.tv_artist, ContextCompat.getColor(activity, R.color.gray))
                    .itemView.setOnClickListener(v -> ToastUtils.getInstance().showToast("歌曲无法播放"));
        } else {
            vh.getView(R.id.isCpView).setVisibility(View.GONE);
        }
    }

    @NonNull
    private View.OnClickListener getListener(Music music, View view, int position) {
        return v -> mListener.onItemClick(music, view, position);
    }


    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new SongHolder(inflateItemView(parent, R.layout.item_music));
    }

    class SongHolder extends BaseViewHolder {
        public SongHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(Music music, View view, int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
