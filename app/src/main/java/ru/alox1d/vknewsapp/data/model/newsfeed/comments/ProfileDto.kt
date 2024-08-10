package ru.alox1d.vknewsapp.data.model.newsfeed.comments

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id") val id: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("photo_100") val avatarUrl: String
)
