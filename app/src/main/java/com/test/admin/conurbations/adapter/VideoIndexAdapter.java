package com.test.admin.conurbations.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.activitys.VideoDetailActivity;
import com.test.admin.conurbations.model.entity.LeVideoData;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.DisplayUtils;

import javax.inject.Inject;

/**
 * VideoIndexAdapter
 * Created by zhouqiong on 2019/4/2.
 */

public class VideoIndexAdapter extends BaseListAdapter<LeVideoData> {

    private Context context;

    @Inject
    public VideoIndexAdapter(Fragment context) {
        super(context);
        this.context = context.getContext();
    }

    @Override
    protected void bindDataToItemView(final BaseViewHolder holder, final LeVideoData data) {
        SimpleDraweeView mNearbyImg = holder.getView(R.id.nearby_img);
        ViewGroup.LayoutParams params = mNearbyImg.getLayoutParams();
        params.width = (DisplayUtils.getScreenWidth(context) - DisplayUtils.dp2px(2, context)) / 2;
        params.height = (params.width) * 8 / 5;
        mNearbyImg.setLayoutParams(params);

        if (!TextUtils.isEmpty(data.dynamicCover)) {
            final Uri uri = Uri.parse(data.dynamicCover);
            if (isNotEqualsUriPath(mNearbyImg, data.dynamicCover)) {
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true)//设置为true将循环播放Gif动画
                        .setOldController(mNearbyImg.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>() {

                            @Override
                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                                mNearbyImg.setTag(R.id.nearby_img, uri);
                            }

                        })
                        .build();

                mNearbyImg.setController(controller);
            }
        }

        holder.setText(R.id.tv_video_title, data.title)
                .setText(R.id.tv_play_count, CommonUtil.formatNumber(data.playCount) + "分享")
                .setText(R.id.tv_like_count, CommonUtil.formatNumber(data.likeCount) + "赞");

        mNearbyImg.setOnClickListener(getListener(data));
    }

    @NonNull
    private View.OnClickListener getListener(final LeVideoData leVideoData) {
        return v -> {
            if (leVideoData == null) return;
            Intent intent = new Intent(context, VideoDetailActivity.class);
            intent.putExtra(VideoDetailActivity.VIDEO_DATA, leVideoData);
            context.startActivity(intent);
        };
    }

    /**
     * 解决fresco 闪屏
     *
     * @param mNearbyImg
     * @param imgUrl
     * @return
     */
    private boolean isNotEqualsUriPath(SimpleDraweeView mNearbyImg, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl) || TextUtils.isEmpty(mNearbyImg.getTag(R.id.nearby_img) + "")) {
            return false;
        }
        return !(mNearbyImg.getTag(R.id.nearby_img) + "").equals(imgUrl);
    }


    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new VideoIndexAdapter.SampleViewHolder((ViewGroup) inflateItemView(parent, R.layout.item_video_index));
    }

    class SampleViewHolder extends BaseViewHolder {
        public SampleViewHolder(ViewGroup parent) {
            super(parent);
        }
    }
}
