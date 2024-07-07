package ru.alox1d.vknewsapp.data.model.newsfeed.likes

import com.google.gson.annotations.SerializedName

data class LikesCountDto(
    @SerializedName("likes") val count: Int
)
