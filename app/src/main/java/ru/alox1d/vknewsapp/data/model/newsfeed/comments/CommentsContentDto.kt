package ru.alox1d.vknewsapp.data.model.newsfeed.comments

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val comments: List<CommentDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>
)
