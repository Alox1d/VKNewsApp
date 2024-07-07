package ru.alox1d.vknewsapp.data.model.newsfeed.likes

import com.google.gson.annotations.SerializedName

data class LikesCountResponseDto(
    @SerializedName("response") val likes: LikesCountDto
)
