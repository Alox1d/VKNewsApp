package ru.alox1d.vknewsapp.data.model.newsfeed.comments

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("from_id") val authorId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long
)
