package com.test.admin.conurbations.model.entity

import com.google.gson.annotations.SerializedName

class BannerResult(@SerializedName("banners")
                   val banners: MutableList<BannerBean>,
                   @SerializedName("code")
                   val code: Int = 0)

class BannerBean(@SerializedName("picUrl")
                 val picUrl: String,
                 @SerializedName("url")
                 val url: String,
                 @SerializedName("targetId")
                 val targetId: String,
                 @SerializedName("backgroundUrl")
                 val backgroundUrl: String,
                 @SerializedName("targetType")
                 val targetType: String,
                 @SerializedName("typeTitle")
                 val typeTitle: String,
                 @SerializedName("monitorType")
                 val monitorType: String,
                 @SerializedName("monitorImpress")
                 val monitorImpress: String,
                 @SerializedName("monitorClick")
                 val monitorClick: String)