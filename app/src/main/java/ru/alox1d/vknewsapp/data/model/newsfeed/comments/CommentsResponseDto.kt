package ru.alox1d.vknewsapp.data.model.newsfeed.comments

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response") val content: CommentsContentDto
)
