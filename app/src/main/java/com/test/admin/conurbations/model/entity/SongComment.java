package com.test.admin.conurbations.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZQiong on 2018/12/9.
 */
public class SongComment {
    public String userId;
    public String avatarUrl;
    public String nick;
    public String commentId;
    public String content;
    public long time;
    public String type;

   public class User{
       @SerializedName("avatarUrl")
       public String avatarUrl;
       @SerializedName("nickname")
       public String nickname;
       @SerializedName("remarkName")
       public String remarkName;
       @SerializedName("userId")
       public String userId;
   }
         
}
