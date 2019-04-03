package com.test.admin.conurbations.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class LeVideoData implements Parcelable {

    public String title;//视频标题
    public String authorImgUrl;//作者头像地址
    public String authorName;//作者名称
    public String authorSignature;//作者签名
    public int authorSex;//作者性别
    public String coverImgUrl;//视频封面图片地址
    public String dynamicCover;//视频动态封面图片地址
    public String videoPlayUrl;//视频播放地址
    public String videoDownloadUrl;//视频下载地址
    public int videoWidth;//视频宽度
    public int videoHeight;//视频高度
    public long playCount;//播放次数
    public long likeCount;//点赞次数
    public long createTime;//创建时间

    //抖音专用
    public String musicImgUrl;//音乐图片
    public String musicName;//音乐名称
    public String musicAuthorName;//音乐作者

    //火山专用
    public String videoDuration;//视频时长(秒拍也用到)
    public String authorCity;//作者所在城市
    public String authorAge;//作者年龄

    //为了更好的效率问题提前格式化内容字段
    public String formatTimeStr = "";
    public String filterTitleStr = "";
    public String filterUserNameStr = "";
    public String formatPlayCountStr = "";
    public String formatLikeCountStr = "";
    public String filterMusicNameStr = "";

    //视频的Base64值
    public int type = 0;//视频类型：1 抖音 2 火山 3 快手 4 秒拍

    public LeVideoData() {
    }


    protected LeVideoData(Parcel in) {
        title = in.readString();
        authorImgUrl = in.readString();
        authorName = in.readString();
        authorSignature = in.readString();
        authorSex = in.readInt();
        coverImgUrl = in.readString();
        dynamicCover = in.readString();
        videoPlayUrl = in.readString();
        videoDownloadUrl = in.readString();
        videoWidth = in.readInt();
        videoHeight = in.readInt();
        playCount = in.readLong();
        likeCount = in.readLong();
        createTime = in.readLong();
        musicImgUrl = in.readString();
        musicName = in.readString();
        musicAuthorName = in.readString();
        videoDuration = in.readString();
        authorCity = in.readString();
        authorAge = in.readString();
        formatTimeStr = in.readString();
        filterTitleStr = in.readString();
        filterUserNameStr = in.readString();
        formatPlayCountStr = in.readString();
        formatLikeCountStr = in.readString();
        filterMusicNameStr = in.readString();
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(authorImgUrl);
        dest.writeString(authorName);
        dest.writeString(authorSignature);
        dest.writeInt(authorSex);
        dest.writeString(coverImgUrl);
        dest.writeString(dynamicCover);
        dest.writeString(videoPlayUrl);
        dest.writeString(videoDownloadUrl);
        dest.writeInt(videoWidth);
        dest.writeInt(videoHeight);
        dest.writeLong(playCount);
        dest.writeLong(likeCount);
        dest.writeLong(createTime);
        dest.writeString(musicImgUrl);
        dest.writeString(musicName);
        dest.writeString(musicAuthorName);
        dest.writeString(videoDuration);
        dest.writeString(authorCity);
        dest.writeString(authorAge);
        dest.writeString(formatTimeStr);
        dest.writeString(filterTitleStr);
        dest.writeString(filterUserNameStr);
        dest.writeString(formatPlayCountStr);
        dest.writeString(formatLikeCountStr);
        dest.writeString(filterMusicNameStr);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LeVideoData> CREATOR = new Creator<LeVideoData>() {
        @Override
        public LeVideoData createFromParcel(Parcel in) {
            return new LeVideoData(in);
        }

        @Override
        public LeVideoData[] newArray(int size) {
            return new LeVideoData[size];
        }
    };
}
